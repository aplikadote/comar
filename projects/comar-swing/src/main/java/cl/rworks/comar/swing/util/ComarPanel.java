/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.rworks.comar.swing.util;

import cl.rworks.comar.swing.ComarSystem;
import com.alee.laf.panel.WebPanel;
import java.awt.FlowLayout;
import java.awt.LayoutManager;

/**
 *
 * @author aplik
 */
public class ComarPanel extends WebPanel {

    public ComarPanel() {
        this(new FlowLayout());
    }

    public ComarPanel(LayoutManager layout) {
        super(layout);
        setBackground(ComarSystem.getInstance().getProperties().getBackgroundColor());
    }
}