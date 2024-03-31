//package ru.astondevs.service.Impl;
//
//import ru.astondevs.exception.ScheduleNotFoundException;
//import ru.astondevs.model.DoctorSchedule;
//import ru.astondevs.repository.DoctorScheduleRepository;
//import ru.astondevs.service.DoctorScheduleService;
//
//import java.util.Collections;
//import java.util.List;
//
//public class DoctorScheduleServiceImpl implements DoctorScheduleService {
//
//    private final DoctorScheduleRepository repository;
//
//    public DoctorScheduleServiceImpl(DoctorScheduleRepository repository) {
//        this.repository = repository;
//    }
//
//    @Override
//    public DoctorSchedule findById(Long id) throws ScheduleNotFoundException {
//        return repository.findById(id).orElseThrow(() -> new ScheduleNotFoundException("Расписание не найдено"));
//    }
//
//    @Override
//    public boolean deleteById(Long id) {
//
//        return false;
//    }
//
//    @Override
//    public List<DoctorSchedule> findAll() {
//        List<DoctorSchedule> scheduleList = repository.findAll();
//        return scheduleList.isEmpty()? Collections.emptyList() : scheduleList;
//    }
//
//    @Override
//    public DoctorSchedule save(DoctorSchedule schedule) {
//
//        return null;
//    }
//
//    @Override
//    public boolean update(DoctorSchedule toUpdated, Long object) {
//        return false;
//    }
//
//
//}
