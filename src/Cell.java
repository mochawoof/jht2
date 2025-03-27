class Cell {
    public byte b;
    public Cell(byte by) {
        b = by;
    }
    public String toString() {
        return Decimal.toHex((int) b);
    }
}