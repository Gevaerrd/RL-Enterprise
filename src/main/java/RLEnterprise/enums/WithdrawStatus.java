/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */

package RLEnterprise.enums;

/**
 *
 * @author Pichau
 */
public enum WithdrawStatus {

    PENDENTE(0),
    APROVADO(1),
    RECUSADO(2);

    private final int code;

    WithdrawStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static WithdrawStatus fromCode(int code) {
        for (WithdrawStatus ws : values()) {
            if (ws.code == code)
                return ws;
        }
        throw new IllegalArgumentException("Código inválido: " + code);
    }

}
