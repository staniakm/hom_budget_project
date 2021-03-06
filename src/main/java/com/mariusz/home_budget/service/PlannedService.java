package com.mariusz.home_budget.service;

import com.mariusz.home_budget.entity.AppUser;
import com.mariusz.home_budget.entity.MoneyHolder;
import com.mariusz.home_budget.entity.PlannedOperation;
import com.mariusz.home_budget.entity.form.PlanForm;
import com.mariusz.home_budget.helpers.MoneyFlowTypes;
import com.mariusz.home_budget.helpers.PeriodicTypes;
import com.mariusz.home_budget.repository.MoneyHoldersRepository;
import com.mariusz.home_budget.repository.PlannedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PlannedService {

    private final PlannedRepository plannedRepository;
    private final MoneyHoldersRepository moneyHoldersRepository;

    @Autowired
    public PlannedService(PlannedRepository plannedRepository
            , MoneyHoldersRepository moneyHoldersRepository) {
        this.plannedRepository = plannedRepository;
        this.moneyHoldersRepository = moneyHoldersRepository;
    }


    public Optional<String> savePlannedOperation(PlanForm planForm, AppUser user) {
        //TODO split validation to separated methods
        PeriodicTypes periodic;
        MoneyFlowTypes moneyFlowTypes;
        LocalDate operationDate;
        BigDecimal operationAmount;
        Long moneySource;

        //enum verification
        if (planForm.getPeriodicity() == null || planForm.getPeriodicity().trim().length() == 0) {
            return Optional.of("Incorrect periodicity");
        } else {
            try {
                periodic = PeriodicTypes.valueOf(planForm.getPeriodicity());
            } catch (IllegalArgumentException ex) {
                return Optional.of("Incorrect periodicity type.");
            }
        }


        if (planForm.getPlanedType() == null || planForm.getPlanedType().trim().length() == 0) {
            return Optional.of("Incorrect planed type");
        } else {
            try {
                moneyFlowTypes = MoneyFlowTypes.valueOf(planForm.getPlanedType());
            } catch (IllegalArgumentException ex) {
                return Optional.of("Incorrect planed type.");
            }
        }


        if (planForm.getMoneyHolder() == null || planForm.getMoneyHolder().trim().length() == 0) {
            return Optional.of("Incorrect money holder selected.");
        } else {
            try {
                moneySource = Long.parseLong(planForm.getMoneyHolder());
            } catch (Exception ex) {
                return Optional.of("Incorrect money holder.");
            }
        }

        //planed date
        if (planForm.getDueDate() == null || planForm.getDueDate().trim().length() == 0) {
            operationDate = LocalDate.now();
        } else {
            try {
                operationDate = LocalDate.parse(planForm.getDueDate());
            } catch (Exception ex) {
                return Optional.of("Date must be valid");
            }
        }

        if (planForm.getAmount() == null || planForm.getAmount().trim().length() == 0) {
            return Optional.of("Amount must be provided");
        } else {
            try {
                operationAmount = new BigDecimal(planForm.getAmount().replace(",", "."));

            } catch (Exception ex) {
                return Optional.of("Amount must be in valid format");
            }
        }

        if (planForm.getDescription() == null || planForm.getDescription().trim().length() == 0) {
            return Optional.of("Description must be filled.");
        }

        Optional<MoneyHolder> moneyHolder = moneyHoldersRepository
                .findByUserAndId(user, moneySource);

        if (!moneyHolder.isPresent()) {
            return Optional.of("Incorrect money holder for logged user");
        }

        PlannedOperation plannedOperation = PlannedOperation.builder()
                .user(user)
                .amount(operationAmount)
                .days(periodic.getDays())
                .description(planForm.getDescription())
                .dueDate(operationDate)
                .periodicity(periodic)
                .planedType(moneyFlowTypes)
                .moneyHolder(moneyHolder.get())
                .active(true)
                .finished(false)
                .build();
        plannedRepository.save(plannedOperation);

        return Optional.empty();
    }


    public List<PlannedOperation> getPlanedActiveOperation(AppUser user) {
        return plannedRepository.findByUserAndActiveTrueAndFinishedFalseOrderByDueDateAsc(user);
    }


    public void deletePlan(Long planId, AppUser user) {
        Optional<PlannedOperation> operation = plannedRepository.findByUserAndIdAndActiveIsTrueAndFinishedIsFalse(user, planId);
        if (operation.isPresent()) {
            PlannedOperation plannedOperation = operation.get();
            plannedOperation.setActive(false);
            plannedRepository.save(plannedOperation);
        }
    }


    public Optional<PlannedOperation> findByUserAndIdAndActiveIsTrueAndFinishedIsFalse(AppUser user, Long id) {
        return plannedRepository.findByUserAndIdAndActiveIsTrueAndFinishedIsFalse(user, id);
    }


    public void savePlannedOperation(PlannedOperation plannedOperation) {
        plannedRepository.save(plannedOperation);
    }
}
