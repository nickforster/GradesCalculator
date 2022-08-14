import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;

public class FaecherView extends JFrame {
    private final JPanel tabellenPanel = new JPanel();
    private final JPanel faecherPanel = new JPanel();
    private final JPanel faecherTotalPanel = new JPanel();
    private final JPanel notenPanel = new JPanel();
    private final JPanel notenTitelPanel = new JPanel();
    private final JPanel notenDurchschnittPanel = new JPanel();
    private final JPanel buttonPanel = new JPanel();

    private final JLabel faecherTitelLabel = new JLabel("Durchschnitte");
    private final JLabel faecherTotalLabel = new JLabel("Total Durchschnitt");
    protected final JLabel faecherTotalWertLabel;
    private final JLabel notenTotalLabel = new JLabel("Durchschnitt");
    protected final JLabel notenTotalWertLabel;

    private final JTable faecherTabelle;
    private final JTable notenTabelle;
    public JComboBox faecherComboBox;

    private final JButton fachLoeschenBtn = new JButton("Fach löschen");
    private final JButton fachZufuegenBtn = new JButton("Fach hinzufügen");
    private final JButton noteLoeschenBtn = new JButton("Note löschen");
    private final JButton noteZufuegenBtn = new JButton("Note hinzufügen");

    public Fach fach; /** Sammlung von Noten **/
    public FachModel fachModel; /** Sammlung von Fächern **/

    public FaecherView(FachModel fm, Fach f) {
        fachModel = fm;
        fach = f;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Notenverwaltung");

        DefaultComboBoxModel comboBoxModel = new DefaultComboBoxModel();
        for (int i = 0; i < fachModel.getFaecherTitel().length; i++) {
            comboBoxModel.addElement(fachModel.getFaecherTitel()[i]);
        }
        faecherComboBox = new JComboBox(comboBoxModel);

        faecherTabelle = new JTable(fachModel);
        JScrollPane faecherScrollPane = new JScrollPane(faecherTabelle);
        notenTabelle = new JTable(fach);
        JScrollPane fachScrollPane = new JScrollPane(notenTabelle);

        faecherTotalWertLabel = new JLabel(fachModel.getDurchschnitt());
        notenTotalWertLabel = new JLabel(fach.getDurchschnitt());

        faecherPanel.setLayout(new BorderLayout());
        faecherPanel.add(BorderLayout.NORTH, faecherTitelLabel);
        faecherPanel.add(BorderLayout.CENTER, faecherScrollPane);

        faecherTotalPanel.setLayout(new GridLayout(1, 2));
        faecherTotalPanel.add(faecherTotalLabel);
        faecherTotalPanel.add(faecherTotalWertLabel);
        faecherPanel.add(BorderLayout.SOUTH, faecherTotalPanel);

        notenTitelPanel.setLayout(new GridLayout(1, 1));
        notenTitelPanel.add(faecherComboBox);

        notenDurchschnittPanel.setLayout(new GridLayout(1, 2));
        notenDurchschnittPanel.add(notenTotalLabel);
        notenDurchschnittPanel.add(notenTotalWertLabel);

        notenPanel.setLayout(new BorderLayout());
        notenPanel.add(BorderLayout.NORTH, notenTitelPanel);
        notenPanel.add(BorderLayout.CENTER, fachScrollPane);
        notenPanel.add(BorderLayout.SOUTH, notenDurchschnittPanel);

        tabellenPanel.setLayout(new GridLayout(1, 2));
        tabellenPanel.add(faecherPanel);
        tabellenPanel.add(notenPanel);

        buttonPanel.setLayout(new GridLayout(1, 4));
        buttonPanel.add(fachLoeschenBtn);
        buttonPanel.add(fachZufuegenBtn);
        buttonPanel.add(noteLoeschenBtn);
        buttonPanel.add(noteZufuegenBtn);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(BorderLayout.CENTER, tabellenPanel);
        getContentPane().add(BorderLayout.SOUTH, buttonPanel);

        pack();
        setVisible(true);

        /*
         * Actionlistener
         */
        faecherComboBox.addActionListener(e -> {
            fach = fachModel.getFach(faecherComboBox.getSelectedIndex());
            notenTabelle.setModel(fach);
            updateDurchschnitt();
        });

        fachZufuegenBtn.addActionListener(e -> {
            new FachZufuegenDialog(fachModel, faecherComboBox, notenTotalWertLabel, faecherTotalWertLabel, fach);
            updateDurchschnitt();
        });

        fachLoeschenBtn.addActionListener(e -> {
            int index = faecherTabelle.getSelectedRow();

            faecherComboBox.removeItem(fachModel.getFach(index).getTitel());

            fachModel.removeFach(index);
            faecherComboBox.setSelectedIndex(0);

            fachModel.fireTableDataChanged();
            updateDurchschnitt();
        });

        noteZufuegenBtn.addActionListener(e -> {
            new NoteZufuegenDialog(fach, notenTotalWertLabel, faecherTotalWertLabel, fachModel);
            updateDurchschnitt();
        });

        noteLoeschenBtn.addActionListener(e -> {
            fach.removeNote(notenTabelle.getSelectedRow());

            fach.fireTableDataChanged();
            fachModel.fireTableDataChanged();
            updateDurchschnitt();
        });

        notenTabelle.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() >= 2) {
                    new NoteBearbeiten(fachModel, notenTabelle, fach, notenTotalWertLabel, faecherTotalWertLabel);
                }
            }
        });

        faecherTabelle.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() >= 2) {
                    new FachBearbeiten(fachModel, faecherTabelle, fach, notenTotalWertLabel, faecherTotalWertLabel, faecherComboBox);
                }
            }
        });
    }

    public void updateDurchschnitt() {
        notenTotalWertLabel.setText(fach.getDurchschnitt());
        faecherTotalWertLabel.setText(fachModel.getDurchschnitt());
        fachModel.fireTableDataChanged();
    }

    public static void main(String[] args) {
        FachModel fachModel = new FachModel();
        fachModel.addFach(new Fach("Mathematik", LocalDate.now(), true, 1));
        fachModel.addFach(new Fach("Englisch", LocalDate.now(), true, 1));
        Fach fach = fachModel.getFach(0);
        fachModel.getFach(0).addNote(new Note("Funktionen", 6, LocalDate.now(), true, 0.5));
        fachModel.getFach(0).addNote(new Note("Exponentialfunktionen", 3, LocalDate.now(), true, 1));
        fachModel.getFach(1).addNote(new Note("Unit1", 5.5, LocalDate.now(), true, 1));
        // TODO alle starteinstellungen löschen ???

        new FaecherView(fachModel, fach);

    }

}

class FachZufuegenDialog extends JDialog {
    private final String[] labelTitel = new String[]{"Titel", "Datum", "Fach zählt", "Gewichtung"};
    private final Object[] inputs = new Object[4];

    private final JPanel inputPanel = new JPanel();

    private final JTextField titelTextField = new JTextField("Neues Fach");
    private final JTextField datumTextField = new JTextField(LocalDate.now().toString());
    private final JCheckBox fachZaehltCheckBox = new JCheckBox();
    private final JTextField gewichtungTextField = new JTextField("1");
    private final JButton zufuegenBtn = new JButton("Fach hinzufügen");
    private final JDialog dialog;

    FachZufuegenDialog(FachModel fachmodel, JComboBox comboBox, JLabel notenTotalWertLabel, JLabel faecherTotalWertLabel, Fach fach) {
        this.dialog = this;
        setTitle("Fach Hinzufügen");
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        fachZaehltCheckBox.setSelected(true);

        inputs[0] = titelTextField;
        inputs[1] = datumTextField;
        inputs[2] = fachZaehltCheckBox;
        inputs[3] = gewichtungTextField;

        inputPanel.setLayout(new GridLayout(4, 2));
        for (int i = 0; i < labelTitel.length; i++) {
            inputPanel.add(new JLabel(labelTitel[i]));
            inputPanel.add((Component) inputs[i]);
        }

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(BorderLayout.CENTER, inputPanel);
        getContentPane().add(BorderLayout.SOUTH, zufuegenBtn);

        pack();
        setVisible(true);

        zufuegenBtn.addActionListener(e -> {
            String titel = titelTextField.getText();
            LocalDate datum = LocalDate.now();
            boolean fachZaehlt = fachZaehltCheckBox.isSelected();
            double gewichtung = 1;

            boolean error = false;

            try {
                datum = LocalDate.parse(datumTextField.getText());
            } catch (Exception exception) {
                new ErrorMessage("Bitte geben sie ein korrektes Datum im Format \"YYYY-MM-DD\" ein.");
                error = true;
            }
            try {
                gewichtung = Double.parseDouble(gewichtungTextField.getText());
            } catch (Exception exception) {
                new ErrorMessage("Bitte geben sie eine Zahl für die Gewichtung ein.");
                error = true;
            }

            if (!error) {
                fachmodel.addFach(new Fach(titel, datum, fachZaehlt, gewichtung));
                comboBox.addItem(titel);

                notenTotalWertLabel.setText(fachmodel.getDurchschnitt());
                faecherTotalWertLabel.setText(fach.getDurchschnitt());

                fachmodel.fireTableDataChanged();
                dialog.dispose();
            }
        });
    }
}

class NoteZufuegenDialog extends JDialog {
    String[] labelTitel = new String[]{"Titel", "Note", "Datum", "Note zählt", "Gewichtung"};
    Object[] inputs = new Object[5];

    private final JPanel inputPanel = new JPanel();

    private final JTextField titelTextField = new JTextField("Neue Note");
    private final JTextField noteTextField = new JTextField("1");
    private final JTextField datumTextField = new JTextField(LocalDate.now().toString());
    private final JCheckBox noteZaehltCheckBox = new JCheckBox();
    private final JTextField gewichtungTextField = new JTextField("1");
    private final JButton zufuegenBtn = new JButton("Note hinzufügen");
    private final JDialog dialog;

    NoteZufuegenDialog(Fach fach, JLabel notenTotalWertLabel, JLabel faecherTotalWertLabel, FachModel fachModel) {
        this.dialog = this;
        setTitle("Note Hinzufügen");
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        noteZaehltCheckBox.setSelected(true);

        inputs[0] = titelTextField;
        inputs[1] = noteTextField;
        inputs[2] = datumTextField;
        inputs[3] = noteZaehltCheckBox;
        inputs[4] = gewichtungTextField;

        inputPanel.setLayout(new GridLayout(5, 2));
        for (int i = 0; i < labelTitel.length; i++) {
            inputPanel.add(new JLabel(labelTitel[i]));
            inputPanel.add((Component) inputs[i]);
        }

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(BorderLayout.CENTER, inputPanel);
        getContentPane().add(BorderLayout.SOUTH, zufuegenBtn);

        pack();
        setVisible(true);

        zufuegenBtn.addActionListener(e -> {
            String titel = titelTextField.getText();
            double note = 1;
            LocalDate datum = LocalDate.now();
            boolean noteZaehlt = noteZaehltCheckBox.isSelected();
            double gewichtung = 1;

            boolean error = false;

            try {
                datum = LocalDate.parse(datumTextField.getText());
            } catch (Exception exception) {
                new ErrorMessage("Bitte geben sie ein korrektes Datum im format \"YYYY-DD-MM\" ein.");
                error = true;
            }
            try {
                note = Double.parseDouble(noteTextField.getText());
            } catch (Exception exception) {
                new ErrorMessage("Bitte geben sie ein korrete Note ein.");
                error = true;
            }
            try {
                gewichtung = Double.parseDouble(gewichtungTextField.getText());
            } catch (Exception exception) {
                new ErrorMessage("Bitte geben sie eine Zahl für die Gewichtung ein.");
                error = true;
            }
            if (note > 6 || note < 1) {
                new ErrorMessage("Die Note muss eine Zahl zwischen 1 und 6 sein");
                error = true;
            }

            if (!error) {
                fach.addNote(new Note(titel, note, datum, noteZaehlt, gewichtung));

                notenTotalWertLabel.setText(fach.getDurchschnitt());
                faecherTotalWertLabel.setText(fachModel.getDurchschnitt());

                fach.fireTableDataChanged();
                fachModel.fireTableDataChanged();

                dialog.dispose();
            }
        });
    }
}

class FachBearbeiten extends JDialog {
    private final String[] labelTitel = new String[]{"Titel", "Datum", "Fach zählt", "Gewichtung"};
    private final Object[] inputs = new Object[4];

    private final JPanel inputPanel = new JPanel();

    private final JCheckBox fachZaehltCheckBox = new JCheckBox();
    private final JTextField titelTextField = new JTextField();
    private final JTextField datumTextField = new JTextField();
    private final JTextField gewichtungTextField = new JTextField("1");
    private final JButton fertigBtn = new JButton("Fertig");
    private final JDialog dialog;

    String titel;
    LocalDate datum;
    boolean fachZaehlt;
    double gewichtung;

    FachBearbeiten(FachModel fachModel, JTable faecherTabelle, Fach fach, JLabel notenTotalWertLabel, JLabel faecherTotalWertLabel, JComboBox faecherComboBox) {
        this.dialog = this;
        Fach aktivesFach = fachModel.getFach(faecherTabelle.getSelectedRow());

        setTitle(aktivesFach.getTitel() + " bearbeiten");
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        titelTextField.setText(aktivesFach.getTitel());
        datumTextField.setText(aktivesFach.getDatum());
        fachZaehltCheckBox.setSelected(aktivesFach.getFachZaehlt());
        gewichtungTextField.setText(String.valueOf(aktivesFach.getGewichtung()));

        datum = LocalDate.parse(datumTextField.getText());
        gewichtung = Double.parseDouble(gewichtungTextField.getText());

        inputs[0] = titelTextField;
        inputs[1] = datumTextField;
        inputs[2] = fachZaehltCheckBox;
        inputs[3] = gewichtungTextField;

        inputPanel.setLayout(new GridLayout(4, 2));
        for (int i = 0; i < labelTitel.length; i++) {
            inputPanel.add(new JLabel(labelTitel[i]));
            inputPanel.add((Component) inputs[i]);
        }

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(BorderLayout.CENTER, inputPanel);
        getContentPane().add(BorderLayout.SOUTH, fertigBtn);

        pack();
        setVisible(true);

        fertigBtn.addActionListener(e -> {
            titel = titelTextField.getText();
            fachZaehlt = fachZaehltCheckBox.isSelected();
            boolean error = false;

            try {
                datum = LocalDate.parse(datumTextField.getText());
            } catch (Exception exception) {
                new ErrorMessage("Bitte geben sie ein korrektes Datum im Format \"YYYY-MM-DD\" ein.");
                error = true;
            }
            try {
                gewichtung = Double.parseDouble(gewichtungTextField.getText());
            } catch (Exception exception) {
                new ErrorMessage("Bitte geben sie eine Zahl für die Gewichtung ein.");
                error = true;
            }

            if (!error) {
                aktivesFach.setTitel(titel);
                aktivesFach.setDatum(datum);
                aktivesFach.setFachZaehlt(fachZaehlt);
                aktivesFach.setGewichtung(gewichtung);

                DefaultComboBoxModel comboBoxModel = new DefaultComboBoxModel();
                for (int i = 0; i < fachModel.getFaecherTitel().length; i++) {
                    comboBoxModel.addElement(fachModel.getFaecherTitel()[i]);
                }
                faecherComboBox.setModel(comboBoxModel);

                notenTotalWertLabel.setText(fach.getDurchschnitt());
                faecherTotalWertLabel.setText(fachModel.getDurchschnitt());

                fachModel.fireTableDataChanged();
                dialog.dispose();
            }
        });
    }
}

class NoteBearbeiten extends JDialog {
    private final String[] labelTitel = new String[]{"Titel", "Note", "Datum", "Note zählt", "Gewichtung"};
    private final Object[] inputs = new Object[5];

    private final JPanel inputPanel = new JPanel();

    private final JCheckBox fachZaehltCheckBox = new JCheckBox();
    private final JTextField titelTextField = new JTextField();
    private final JTextField noteTextField = new JTextField();
    private final JTextField datumTextField = new JTextField();
    private final JTextField gewichtungTextField = new JTextField("1");
    private final JButton fertigBtn = new JButton("Fertig");
    private final JDialog dialog;

    String titel;
    double note;
    LocalDate datum;
    boolean fachZaehlt;
    double gewichtung;

    public NoteBearbeiten(FachModel fachModel, JTable faecherTabelle, Fach fach, JLabel notenTotalWertLabel, JLabel faecherTotalWertLabel) {
        this.dialog = this;
        Note aktiveNote = fach.getNote(faecherTabelle.getSelectedRow());

        setTitle(aktiveNote.getTitel() + " bearbeiten");
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        titelTextField.setText(aktiveNote.getTitel());
        noteTextField.setText(String.valueOf(aktiveNote.getNote()));
        datumTextField.setText(aktiveNote.getDatum());
        fachZaehltCheckBox.setSelected(aktiveNote.getNoteZaehlt());
        gewichtungTextField.setText(String.valueOf(aktiveNote.getGewichtung()));

        datum = LocalDate.parse(datumTextField.getText());
        gewichtung = Double.parseDouble(gewichtungTextField.getText());

        inputs[0] = titelTextField;
        inputs[1] = noteTextField;
        inputs[2] = datumTextField;
        inputs[3] = fachZaehltCheckBox;
        inputs[4] = gewichtungTextField;

        inputPanel.setLayout(new GridLayout(5, 2));
        for (int i = 0; i < labelTitel.length; i++) {
            inputPanel.add(new JLabel(labelTitel[i]));
            inputPanel.add((Component) inputs[i]);
        }

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(BorderLayout.CENTER, inputPanel);
        getContentPane().add(BorderLayout.SOUTH, fertigBtn);

        pack();
        setVisible(true);

        fertigBtn.addActionListener(e -> {
            titel = titelTextField.getText();
            fachZaehlt = fachZaehltCheckBox.isSelected();
            boolean error = false;

            try {
                datum = LocalDate.parse(datumTextField.getText());
            } catch (Exception exception) {
                new ErrorMessage("Bitte geben sie ein korrektes Datum im Format \"YYYY-MM-DD\" ein.");
                error = true;
            }
            try {
                note = Double.parseDouble(noteTextField.getText());
            } catch (Exception exception) {
                new ErrorMessage("Bitte geben sie ein korrete Note ein.");
                error = true;
            }
            try {
                gewichtung = Double.parseDouble(gewichtungTextField.getText());
            } catch (Exception exception) {
                new ErrorMessage("Bitte geben sie eine Zahl für die Gewichtung ein.");
                error = true;
            }

            if (note > 6 || note < 1) {
                new ErrorMessage("Die Note muss eine Zahl zwischen 1 und 6 sein");
                error = true;
            }

            if (!error) {
                aktiveNote.setTitel(titel);
                aktiveNote.setDatum(datum);
                aktiveNote.setNote(note);
                aktiveNote.setNoteZaehlt(fachZaehlt);
                aktiveNote.setGewichtung(gewichtung);

                notenTotalWertLabel.setText(fach.getDurchschnitt());
                faecherTotalWertLabel.setText(fachModel.getDurchschnitt());

                fachModel.fireTableDataChanged();
                fach.fireTableDataChanged();

                dialog.dispose();
            }
        });
    }
}

class ErrorMessage extends JDialog {
    ErrorMessage(String message) {
        setTitle("Error");
        JLabel label = new JLabel(message);
        getContentPane().add(label);
        pack();
        setVisible(true);
    }
}
