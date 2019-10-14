public final class Student {
    private final String plab;
    private final String id; 
    private final int group;

    public Student(String plab, String id, int group) {
        this.plab = plab;
        this.id = id;
        this.group = group;
    }

    public String getPlab() {
        return this.plab;
    }

    public String getId() {
        return this.id;
    }

    public int getGroup() {
        return this.group;
    }
    
    @Override
    public String toString() {
        return plab + "," + id + "," + group;
    }
}
