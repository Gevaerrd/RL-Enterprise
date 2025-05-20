/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package RLEnterprise.services;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import RLEnterprise.entities.AfilliateCode;
import RLEnterprise.repositories.AfilliateCodeRepository;

@Service
public class AfilliateCodeService {

    @Autowired
    AfilliateCodeRepository acr;

    public List<AfilliateCode> findAll() {
        return acr.findAll();
    }

    public AfilliateCode findById(Long id) {
        return acr.findById(id).get();
    }

    public String generateCode() {
        int codeLength = 9;
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        String code;

        do {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < codeLength; i++) {
                int index = random.nextInt(characters.length());
                builder.append(characters.charAt(index));
            }
            code = builder.toString();
        } while (existsByCode(code));

        return code;
    }

    public AfilliateCode findByCode(String code) {
        return acr.findByCode(code);
    }

    public AfilliateCode save(AfilliateCode code) {
        return acr.save(code);
    }

    public void deleteById(Long id) {
        acr.deleteById(id);
    }

    public boolean existsByCode(String code) {
        return acr.existsByCode(code);
    }

}
