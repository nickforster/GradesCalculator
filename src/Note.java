import java.time.LocalDate;

public class Note {
    private String titel;
    private double note;
    private LocalDate datum;
    private boolean noteZaehlt;
    private double gewichtung;

    public Note(String titel, double note, LocalDate datum, boolean noteZaehlt, double gewichtung) {
        this.titel = titel;
        this.note = note;
        this.datum = datum;
        this.noteZaehlt = noteZaehlt;
        this.gewichtung = gewichtung;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public double getNote() {
        return note;
    }

    public void setNote(double note) {
        this.note = note;
    }

    public String getDatum() {
        return String.valueOf(datum);
    }

    public void setDatum(LocalDate datum) {
        this.datum = datum;
    }

    public boolean getNoteZaehlt() {
        return noteZaehlt;
    }

    public void setNoteZaehlt(boolean noteZaehlt) {
        this.noteZaehlt = noteZaehlt;
    }

    public double getGewichtung() {
        return gewichtung;
    }

    public void setGewichtung(double gewichtung) {
        this.gewichtung = gewichtung;
    }
}
