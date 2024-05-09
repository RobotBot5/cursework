package com.rtumirea.KazakovIG.cursework.services.impl;

import com.rtumirea.KazakovIG.cursework.domain.entities.OrderEntity;
import com.rtumirea.KazakovIG.cursework.domain.entities.ScheduleEntity;
import com.rtumirea.KazakovIG.cursework.domain.enums.OrderStatus;
import com.rtumirea.KazakovIG.cursework.domain.enums.ScheduleStatus;
import com.rtumirea.KazakovIG.cursework.repositories.OrderRepository;
import com.rtumirea.KazakovIG.cursework.repositories.ScheduleRepository;
import com.rtumirea.KazakovIG.cursework.services.OrderService;
import com.rtumirea.KazakovIG.cursework.services.ScheduleService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    private ScheduleRepository scheduleRepository;

    private OrderRepository orderRepository;

    private OrderService orderService;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository, OrderRepository orderRepository, OrderService orderService) {
        this.scheduleRepository = scheduleRepository;
        this.orderRepository = orderRepository;
        this.orderService = orderService;
    }

    @Override
    public void generateSchedule(LocalDate startDate, LocalDate endDate, int intervalInMinutes) {
        LocalDate currentDate = startDate;

        LocalTime workStartTime = LocalTime.of(9, 0); // 9:00 утра
        LocalTime workEndTime = LocalTime.of(21, 0); // 9:00 вечера

        LocalTime currentTime = workStartTime;

        while (currentDate.isBefore(endDate.plusDays(1))) {
            if (currentTime.isBefore(workEndTime)) {
                scheduleRepository.save(ScheduleEntity.builder()
                        .day(currentDate)
                        .startTime(currentTime)
                        .endTime(currentTime.plusMinutes(intervalInMinutes))
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
    public List<ScheduleEntity> getCurrentClientsSlots() {
        List<OrderEntity> orderEntities = orderService.findByCurrentClientAndStatus(OrderStatus.AWAITING_CAR);
        return orderEntities.stream()
                .map(orderEntity -> scheduleRepository.findByOrderEntity(orderEntity).get())
                .collect(Collectors.toList());

    }

    @Override
    public List<ScheduleEntity> getCurrentAutomechSlots() {
        List<OrderEntity> orderEntities = orderService.findByCurrentAutomech();
        return orderEntities.stream()
                .map(orderEntity -> scheduleRepository.findByOrderEntity(orderEntity).get())
                .collect(Collectors.toList());
    }
}
