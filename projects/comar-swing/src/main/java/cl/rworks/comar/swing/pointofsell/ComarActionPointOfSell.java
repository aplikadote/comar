/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.rworks.comar.swing.pointofsell;

import cl.rworks.comar.swing.ComarFrame;
import cl.rworks.comar.swing.ComarSystem;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

/**
 *
 * @author rgonzalez
 */
public class ComarActionPointOfSell extends AbstractAction{

    public ComarActionPointOfSell(){
        putValue(NAME, "Punto de Venta");
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        ComarFrame frame = ComarSystem.getInstance().getFrame();
        frame.getPanelCard().showCard("POS");
    }
    
}
