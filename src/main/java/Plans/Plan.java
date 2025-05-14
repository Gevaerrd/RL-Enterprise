/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package Plans;

/**
 *
 * @author Pichau
 */
public abstract class Plan {

    private String planName;
    private Integer modulesToFree;

    public Plan(String planName, Integer modulesToFree) {
        this.planName = planName;
        this.modulesToFree = modulesToFree;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public Integer getModulesToFree() {
        return modulesToFree;
    }

    public void setModulesToFree(Integer modulesToFree) {
        this.modulesToFree = modulesToFree;
    }

}
