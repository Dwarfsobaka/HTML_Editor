package editor.listeners;

import editor.View;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

public class UndoMenuListener implements MenuListener {
    private View view;
    private JMenuItem undoMenuItem;
    private JMenuItem redoMenuItem;

    public UndoMenuListener(View view, JMenuItem undoMenuItem, JMenuItem redoMenuItem) {
        this.view = view;
        this.undoMenuItem = undoMenuItem;
        this.redoMenuItem = redoMenuItem;
    }

    @Override
    public void menuSelected(MenuEvent e) {
        boolean viewCanUndo = view.canUndo();
        boolean viewCanRedo = view.canRedo();

        if(viewCanUndo){
            undoMenuItem.setEnabled(true);

        }
        else {undoMenuItem.setEnabled(false);}
        if(viewCanRedo){
            redoMenuItem.setEnabled(true);

        }
        else { redoMenuItem.setEnabled(false);}

    }

    @Override
    public void menuDeselected(MenuEvent e) {

    }

    @Override
    public void menuCanceled(MenuEvent e) {

    }

}
