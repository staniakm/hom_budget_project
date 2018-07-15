package com.mariusz.home_budget.service;

import com.mariusz.home_budget.entity.AppUser;
import com.mariusz.home_budget.entity.Investment;
import com.mariusz.home_budget.entity.form.InvestmentForm;
import com.mariusz.home_budget.mapper.ObjectMapper;
import com.mariusz.home_budget.repository.InvestmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class InvestmentServiceImpl implements InvestmentService {

    private final ObjectMapper mapper;
    private final InvestmentRepository investmentRepository;

    @Autowired
    public InvestmentServiceImpl(ObjectMapper mapper, InvestmentRepository investmentRepository) {
        this.mapper = mapper;
        this.investmentRepository = investmentRepository;
    }

    /**
     * Sum total amount of money from all investments
     * @param user - current logged user
     * @return - BigDecimal as a investments sum
     */
    @Override
    public BigDecimal getInvestmentsSum(AppUser user) {
        return getInvestments(user).stream().map(Investment::getAmount).reduce(BigDecimal.ZERO,BigDecimal::add);
    }

    /**
     * Save new "investment" in DB
     * @param investmentForm (basic information about new investment. Provided by web form)
     * @param user (current logged user)
     * @return - if Optional is not empty then error occur else investment is saved in DB.
     */
    @Override
    public Optional<String> addInvestment(InvestmentForm investmentForm, AppUser user) {

        BigDecimal amount = new BigDecimal(investmentForm.getAmount());
        LocalDate endDate = investmentForm.getDate().plusDays(investmentForm.getInvestmentLength().getDays()*investmentForm.getLength());
        BigDecimal percentage = new BigDecimal(investmentForm.getPercentage());

        if (amount.compareTo(BigDecimal.ZERO)<=0){
            return Optional.of("Amount can't be zero or less then zero");
        }

        if (percentage.compareTo(BigDecimal.ZERO)<=0){
            return Optional.of("Percentage can't be zero or less then zero");
        }

        Investment investment = mapper.mapToInvestment(user, amount,endDate,percentage, investmentForm);
        investmentRepository.save(investment);

        return Optional.empty();
    }


    /**
     * Get list of  user all investments.
     * @param user (current logged user)
     * @return - list of investments
     */
    @Override
    public List<Investment> getInvestments(AppUser user) {
        return investmentRepository.findAllByUserAndActiveTrue(user);
    }

    /**
     * Get investment by id. Only investments assigned to current user can be returned.
     * @param user (current logged user)
     * @param investmentId (investment id)
     * @return - return investment
     */
    @Override
    public Investment getInvestmentsById(AppUser user, Long investmentId) {
        return investmentRepository.findByUserAndId(user, investmentId);
    }



}
