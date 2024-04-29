/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Calendario;

import com.toedter.calendar.JCalendar;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Usuario
 */
public class Calendario extends JDialog{
    public Calendario(JFrame parent) {
        super(parent, "Calendario", true);
        setSize(400, 400);
        setLocationRelativeTo(parent);
        
        // Create the calendar and add it to the dialog
        JCalendar calendar = new JCalendar();
        add(calendar);
    }
}
