package editor;

import editor.listeners.FrameListener;
import editor.listeners.TabbedPaneChangeListener;
import editor.listeners.UndoListener;

import javax.swing.*;
import javax.swing.text.html.HTMLDocument;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class View extends JFrame implements ActionListener {
   private Controller controller;
   private JTabbedPane tabbedPane = new JTabbedPane();

    public UndoListener getUndoListener() {
        return undoListener;
    }

   private JTextPane htmlTextPane = new JTextPane();
   private JEditorPane plainTextPane = new JEditorPane();
   private UndoManager undoManager = new UndoManager();
   private UndoListener undoListener = new UndoListener(undoManager);



    public View(){
    try {
        UIManager.setLookAndFeel (UIManager.getSystemLookAndFeelClassName ());
    } catch (ClassNotFoundException | IllegalAccessException | UnsupportedLookAndFeelException | InstantiationException e) {
        ExceptionHandler.log(e);
    }
}
    public Controller getController() {

        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      String command =  e.getActionCommand();
      if (command.equals("Новый")){
          controller.createNewDocument();
                }
      else if(command.equals("Открыть")){
         controller.openDocument();
      }
      else if(command.equals("Сохранить")){
          controller.saveDocument();
      }
      else if(command.equals("Сохранить как...")){
          controller.saveDocumentAs();
      }
      else if(command.equals("Выход")){
          controller.exit();
      }
      else if (command.equals("О программе")){
            showAbout();
      }
    }
    public void init(){
        initGui();
        addWindowListener(new FrameListener(this));
        setVisible(true);
    }

    public void exit(){
      controller.exit();
    }
    public void initMenuBar(){
        JMenuBar panelMenu = new JMenuBar();
        MenuHelper.initFileMenu(this, panelMenu);
        MenuHelper.initEditMenu(this, panelMenu);
        MenuHelper.initStyleMenu(this, panelMenu);
        MenuHelper.initAlignMenu(this, panelMenu);
        MenuHelper.initColorMenu(this, panelMenu);
        MenuHelper.initFontMenu(this, panelMenu);
        MenuHelper.initHelpMenu(this, panelMenu);
        getContentPane().add(panelMenu,  BorderLayout.NORTH);
    }

    public void initEditor(){
        htmlTextPane.setContentType("text/html");
        JScrollPane paneHTML = new JScrollPane(htmlTextPane);
        tabbedPane.addTab("HTML", paneHTML);

        JScrollPane paneText = new JScrollPane(plainTextPane);
        tabbedPane.addTab( "Текст", paneText);

        tabbedPane.setPreferredSize(new Dimension(600, 600));
        tabbedPane.addChangeListener(new TabbedPaneChangeListener(this));

        getContentPane().add(tabbedPane,  BorderLayout.CENTER);

    }
    public void initGui(){
        initMenuBar();
        initEditor();
        pack();
    }

    public void selectedTabChanged() {
        int select = tabbedPane.getSelectedIndex();
        if (select == 0)
        {
        controller.setPlainText(plainTextPane.getText());
    }
    else {
        plainTextPane.setText(controller.getPlainText());
    }
         resetUndo();
    }
    public boolean canRedo(){
        if (undoManager.canRedo()){
            return true;
        }
            else  return false;
    }

    public boolean canUndo(){
            if (undoManager.canUndo()){
        return true;
    }
            else  return false;
    }

    public void undo(){
        try{
            undoManager.undo();}
    catch (Exception e) {
            ExceptionHandler.log(e);
        }
    }

    public void redo() {
        try {
            undoManager.redo();
        } catch (Exception e) {
            ExceptionHandler.log(e);
        }
    }

    public void resetUndo(){
        undoManager.discardAllEdits();
    }

    public boolean isHtmlTabSelected(){
         if(tabbedPane.getSelectedIndex() == 0){
          return true;
         }
         else {
             return false;
         }
    }

   public void selectHtmlTab(){
       tabbedPane.setSelectedIndex(0);
       resetUndo();
   }

   public void update(){
       HTMLDocument doc = controller.getDocument();
       htmlTextPane.setDocument(doc);
   }

   public void  showAbout(){
       JOptionPane.showMessageDialog(tabbedPane,  "Это редактор HTML", "Сообщение",JOptionPane.INFORMATION_MESSAGE);

   }
}

