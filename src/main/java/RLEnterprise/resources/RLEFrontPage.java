/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package RLEnterprise.resources;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Pichau
 */
@Controller
@RequestMapping()
public class RLEFrontPage {

    @GetMapping()
    public String home() {
        return "FrontPage";
    }
}
