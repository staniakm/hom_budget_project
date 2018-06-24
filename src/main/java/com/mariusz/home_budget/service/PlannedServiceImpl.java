package com.mariusz.home_budget.service;

import com.mariusz.home_budget.entity.AppUser;
import com.mariusz.home_budget.entity.MoneyHolder;
import com.mariusz.home_budget.entity.PlannedOperation;
import com.mariusz.home_budget.entity.form.PlanForm;
import com.mariusz.home_budget.helpers.MoneyFlowTypes;
import com.mariusz.home_budget.helpers.PeriodicTypes;
import com.mariusz.home_budget.repository.MoneyHoldersRepository;
import com.mariusz.home_budget.repository.PlannedRepository;
import com.mariusz.home_budget.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PlannedServiceImpl implements PlannedService {

    private final PlannedRepository plannedRepository;
    private  final UserRepository userRepository;
    private final MoneyHoldersRepository moneyHoldersRepository;

    @Autowired
    public PlannedServiceImpl(PlannedRepository plannedRepository, UserRepository userRepository, MoneyHoldersRepository moneyHoldersRepository) {
        this.plannedRepository = plannedRepository;
        this.userRepository = userRepository;
        this.moneyHoldersRepository = moneyHoldersRepository;
    }


    @Override
    public Optional<String> savePlannedOperation(PlanForm planForm, String userName) {

        //TODO split validation to separated methods

        PeriodicTypes periodic;
        MoneyFlowTypes moneyFlowTypes;
        LocalDate operationDate;
        BigDecimal operationAmount;
        Long moneySource;

        //enum verification
        if (planForm.getPeriodicity()==null || planForm.getPeriodicity().trim().length()==0){
            return Optional.of("Incorrect periodicity");
        }else {
            try {
                periodic = PeriodicTypes.valueOf(planForm.getPeriodicity());
            }catch (IllegalArgumentException ex){
                return Optional.of("Incorrect periodicity type.");
            }
        }


        if (planForm.getPlanedType()==null || planForm.getPlanedType().trim().length()==0){
            return Optional.of("Incorrect planed type");
        }else {
            try {
                moneyFlowTypes = MoneyFlowTypes.valueOf(planForm.getPlanedType());
            }catch (IllegalArgumentException ex){
                return Optional.of("Incorrect planed type.");
            }
        }



        if (planForm.getMoneyHolder()==null || planForm.getMoneyHolder().trim().length()==0){
            return Optional.of("Incorrect money holder selected.");
        }else {
            try {
                moneySource = Long.parseLong(planForm.getMoneyHolder());
            }catch (Exception ex){
                return Optional.of("Incorrect money holder.");
            }
        }

        //planed date
        if (planForm.getDueDate()==null || planForm.getDueDate().trim().length()==0 ){
            operationDate = LocalDate.now();
        }else {
            try {
                operationDate = LocalDate.parse(planForm.getDueDate());
            }catch (Exception ex){
                return Optional.of("Date must be valid");
            }
        }

        if (planForm.getAmount()==null || planForm.getAmount().trim().length()==0){
            return Optional.of("Amount must be provided");
        }else {
            try {
                operationAmount = new BigDecimal(planForm.getAmount().replace(",","."));

            }catch (Exception ex){
                return Optional.of("Amount must be in valid format");
            }
        }

        if (planForm.getDescription()==null || planForm.getDescription().trim().length()==0){
            return Optional.of("Description must be filled.");
        }

        //user
        Optional<AppUser> user = userRepository.findByName(userName);
        if (!user.isPresent()){
            return Optional.of("User details are incorrect. Please login again.");
        }

        Optional<MoneyHolder> moneyHolder = moneyHoldersRepository
                .findByUserAndId(user.get().getId(),moneySource);

        if (!moneyHolder.isPresent()){
            return Optional.of("Incorrect money holder for logged user");
        }

        PlannedOperation plannedOperation = PlannedOperation.builder()
                .user(user.get())
                .amount(operationAmount)
                .days(periodic.getDays())
                .description(planForm.getDescription())
                .dueDate(operationDate)
                .periodicity(periodic)
                .planedType(moneyFlowTypes)
                .moneyHolder(moneyHolder.get())
                .isActive(true)
                .isFinished(false)
                .build();
        plannedRepository.save(plannedOperation);

        return Optional.empty();
    }

    @Override
    public List<PlannedOperation> getPlanedActiveOperation(AppUser user) {
        return plannedRepository.getPlanedActivitiesOperations(user.getId());
//
//        PlannedOperation plannedOperation = PlannedOperation.builder()
//                .planedType(MoneyFlowTypes.EXPENSE)
//                .periodicity(PeriodicTypes.DAILY)
//                .dueDate(LocalDate.now())
//                .description("Test")
//                .days(1)
//                .amount(BigDecimal.valueOf(14.67))
//                .user(user)
//                .isActive(true)
//                .isFinished(false)
//                .moneyHolder(moneyHoldersRepository.findByUserAndId(user.getId(),1L).get())
//                .build();
//
//        return new ArrayList<PlannedOperation>(){{
//            add(plannedOperation);
//        }};

    }
}
