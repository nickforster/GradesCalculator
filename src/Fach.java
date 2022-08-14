import javax.swing.table.AbstractTableModel;
import java.time.LocalDate;
import java.util.Vector;

public class Fach extends AbstractTableModel {
    private String titel;
    private LocalDate datum;
    private boolean fachZaehlt;
    private double gewichtung;
    private final Vector<Note> noten;
    private final String[] tabellenTitel;

    public Fach(String titel, LocalDate datum, boolean fachZaehlt, double gewichtung) {
        noten = new Vector<>();
        tabellenTitel = new String[]{"Bezeichnung", "Gewichtung", "Datum", "Note"};
        this.titel = titel;
        this.datum = datum;
        this.fachZaehlt = fachZaehlt;
        this.gewichtung = gewichtung;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getDatum() {
        return String.valueOf(datum);
    }

    public void setDatum(LocalDate datum) {
        this.datum = datum;
    }

    public boolean getFachZaehlt() {
        return fachZaehlt;
    }

    public void setFachZaehlt(boolean fachZaehlt) {
        this.fachZaehlt = fachZaehlt;
    }

    public double getGewichtung() {
        return gewichtung;
    }

    public void setGewichtung(double gewichtung) {
        this.gewichtung = gewichtung;
    }

    public String getDurchschnitt() {
        double durchschnitt = 0;
        double gesamtGewichtung = 0;
        for (Note n : noten) {
            if (n.getNoteZaehlt()) {
                durchschnitt += n.getNote() * n.getGewichtung();
                gesamtGewichtung += n.getGewichtung();
            }
        }
        return String.format("%.2f", durchschnitt / gesamtGewichtung);
    }

    public void addNote(Note note) {
        noten.add(note);
    }

    public void removeNote(int index) {
        noten.remove(index);
    }

    public Note getNote(int index){
        return noten.get(index);
    }

    @Override
    public int getRowCount() {
        return noten.size();
    }

    @Override
    public int getColumnCount() {
        return tabellenTitel.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0: return noten.get(rowIndex).getTitel();
            case 1: return noten.get(rowIndex).getGewichtung();
            case 2: return noten.get(rowIndex).getDatum();
            case 3: return noten.get(rowIndex).getNote();
            default: return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return tabellenTitel[column];
    }
}
