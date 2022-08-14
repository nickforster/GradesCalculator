import javax.swing.table.AbstractTableModel;
import java.util.Vector;

public class FachModel extends AbstractTableModel {

    protected Vector<Fach> faecher;
    private final String[] title;

    FachModel(){
        faecher = new Vector<>();
        title = new String[]{"Bezeichnung", "Gewichtung", "Datum", "⌀"};
    }

    @Override
    public int getRowCount() {
        return faecher.size();
    }

    @Override
    public int getColumnCount() {
        return title.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0: return faecher.get(rowIndex).getTitel();
            case 1: return faecher.get(rowIndex).getGewichtung();
            case 2: return faecher.get(rowIndex).getDatum();
            case 3: return faecher.get(rowIndex).getDurchschnitt();
            default: return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return title[column];
    }

    public void addFach(Fach fach) {
        faecher.add(fach);
    }

    public void removeFach(int i) {
        faecher.remove(i);
    }

    public Fach getFach(int i){
        return faecher.get(i);
    }

    public String getDurchschnitt(){
        double durchschnitt = 0;
        double gesamtGewichtung = 0;
        for (Fach f : faecher) {
            if (f.getFachZaehlt()) {
                durchschnitt += Double.parseDouble(f.getDurchschnitt()) * f.getGewichtung();
                gesamtGewichtung += f.getGewichtung();
            }
        }
        return String.format("%.2f", durchschnitt / gesamtGewichtung);
    }

    public String[] getFaecherTitel(){
        String[] faecherTitel = new String[faecher.size()];
        for (int i = 0; i < faecher.size(); i++) {
            faecherTitel[i] = faecher.get(i).getTitel();
        }
        return faecherTitel;
    }
}
