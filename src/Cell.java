class Cell {
    public byte b;
    public Cell(byte by) {
        b = by;
    }
    public String toString() {
        return String.format("%02X", (int) b);
    }
}