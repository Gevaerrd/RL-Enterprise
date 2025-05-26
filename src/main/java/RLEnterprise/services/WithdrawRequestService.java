/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package RLEnterprise.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import RLEnterprise.entities.WithdrawRequest;
import RLEnterprise.repositories.WithdrawRequestRepository;

@Service
public class WithdrawRequestService {

    @Autowired
    private WithdrawRequestRepository withdrawRequestRepository;

    public WithdrawRequest save(WithdrawRequest withdrawRequest) {
        return withdrawRequestRepository.save(withdrawRequest);
    }

    public List<WithdrawRequest> findAll() {
        return withdrawRequestRepository.findAll();
    }

    public Optional<WithdrawRequest> findById(Long id) {
        return withdrawRequestRepository.findById(id);
    }

    public void deleteById(Long id) {
        withdrawRequestRepository.deleteById(id);
    }
}