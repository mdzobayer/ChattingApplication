/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModernUI;

/**
 *
 * @author Zobayer
 */
public class test {
    public static void main(String [] args) {
        try {
            Errormsg ch = new Errormsg("HEllo");
            ch.setVisible(true);
        }
        catch(Exception e) {
            System.out.println("Error ");
        }
    }
}
