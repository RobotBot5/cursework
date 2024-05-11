package com.rtumirea.KazakovIG.cursework.services.impl;

import com.rtumirea.KazakovIG.cursework.domain.entities.CarEntity;
import com.rtumirea.KazakovIG.cursework.domain.entities.OrderEntity;
import com.rtumirea.KazakovIG.cursework.domain.entities.ScheduleEntity;
import com.rtumirea.KazakovIG.cursework.domain.enums.OrderStatus;
import com.rtumirea.KazakovIG.cursework.domain.enums.ScheduleStatus;
import com.rtumirea.KazakovIG.cursework.repositories.CarRepository;
import com.rtumirea.KazakovIG.cursework.repositories.OrderRepository;
import com.rtumirea.KazakovIG.cursework.repositories.ScheduleRepository;
import com.rtumirea.KazakovIG.cursework.services.OrderService;
import com.rtumirea.KazakovIG.cursework.services.ScheduleService;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    private ScheduleRepository scheduleRepository;

    private OrderRepository orderRepository;

    private OrderService orderService;

    private CarRepository carRepository;

    private LocalDate[] nonWorkingDays;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository, OrderRepository orderRepository,
                               OrderService orderService, CarRepository carRepository) {
        this.scheduleRepository = scheduleRepository;
        this.orderRepository = orderRepository;
        this.orderService = orderService;
        this.carRepository = carRepository;
        nonWorkingDays = new LocalDate[] {LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 1, 2), LocalDate.of(2024, 1, 3),
                LocalDate.of(2024, 1, 4), LocalDate.of(2024, 1, 5),
                LocalDate.of(2024, 1, 6), LocalDate.of(2024, 1, 7),
                LocalDate.of(2024, 1, 8), LocalDate.of(2024, 2, 23),
                LocalDate.of(2024, 3, 8), LocalDate.of(2024, 5, 1),
                LocalDate.of(2024, 5, 9), LocalDate.of(2024, 6, 12),
                LocalDate.of(2024, 11, 4)};
    }

    @Override
    public void generateScheduleForMonth(LocalDate startDate, int intervalInMinutes) {
        LocalDate currentDate = startDate;
        LocalDate endDate = startDate.plusDays(startDate.getMonth().length(false));

        LocalTime workStartTime = LocalTime.of(9, 0); // 9:00 утра
        LocalTime workEndTime = LocalTime.of(21, 0); // 9:00 вечера

        LocalTime currentTime = workStartTime;

        List<LocalDate> nonWorkingDaysList = Arrays.stream(nonWorkingDays)
                .map(localDate -> localDate.withYear(startDate.getYear())).collect(Collectors.toList());

        while (currentDate.isBefore(endDate)) {
            if (currentTime.isBefore(workEndTime) && !nonWorkingDaysList.contains(currentDate) &&
                    !currentDate.getDayOfWeek().equals(DayOfWeek.SATURDAY) &&
                    !currentDate.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                scheduleRepository.save(ScheduleEntity.builder()
                        .day(currentDate)
                        .startTime(currentTime)
                        .status(ScheduleStatus.FREE)
                        .build());
                currentTime = currentTime.plusMinutes(intervalInMinutes);
            }
            else {
                currentTime = workStartTime;
                currentDate = currentDate.plusDays(1);
            }
        }
    }

    @Override
    public List<ScheduleEntity> getFreeSlots() {
        List<ScheduleEntity> scheduleEntities = scheduleRepository.findAllByStatus(ScheduleStatus.FREE);
        return scheduleEntities.stream()
                .filter(scheduleEntity ->
                        scheduleEntity.getDay().isAfter(LocalDate.now()))
                .collect(Collectors.toList());
    }

    @Override
    public void bookSlot(Long slotId, Long orderId) {
        OrderEntity orderEntity = orderRepository.findById(orderId).get();
        orderEntity.setOrderStatus(OrderStatus.AWAITING_CAR);
        orderRepository.save(orderEntity);
        ScheduleEntity scheduleEntity = scheduleRepository.findById(slotId).get();
        scheduleEntity.setStatus(ScheduleStatus.BOOKED);
        scheduleEntity.setOrderEntity(orderEntity);
        scheduleRepository.save(scheduleEntity);
    }

    @Override
    public List<ScheduleEntity> getCurrentAutomechSlots() {
        List<OrderEntity> orderEntities = orderService.findByCurrentAutomech();
        return orderEntities.stream()
                .map(orderEntity -> scheduleRepository.findByOrderEntity(orderEntity).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteBookedOrder(Long orderId) {
        OrderEntity orderEntity = orderRepository.findById(orderId).get();
        ScheduleEntity scheduleEntity = scheduleRepository.findByOrderEntity(orderEntity).get();
        scheduleEntity.setStatus(ScheduleStatus.FREE);
        scheduleEntity.setOrderEntity(null);
        scheduleRepository.save(scheduleEntity);
        CarEntity carEntity = orderEntity.getCarEntity();
        carEntity.setCarReady(false);
        carRepository.save(carEntity);
        orderRepository.delete(orderEntity);
    }

    @Override
    public List<ScheduleEntity> getCurrentClientSlots() {
        List<OrderEntity> orderEntities = orderService.findByCurrentClient();
        return orderEntities.stream()
                .map(orderEntity -> scheduleRepository.findByOrderEntity(orderEntity).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public List<ScheduleEntity> getAllSlots() {
        return StreamSupport.stream(
                scheduleRepository.findAll()
                        .spliterator(),
                        false)
                .collect(Collectors.toList());
    }

    @Override
    public void addMonth(int intervalInMinutes) {
        LocalDate lastDateInRepo = scheduleRepository.findFirstByOrderByIdDesc().get().getDay();
        generateScheduleForMonth(lastDateInRepo.plusMonths(1).withDayOfMonth(1), intervalInMinutes);
    }
}
