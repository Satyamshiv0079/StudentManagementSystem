package model;

public class Student {

    private int    id;
    private String name;
    private String rollNo;
    private String course;
    private String branch;
    private int    semester;
    private String email;
    private String phone;

    public Student(String name, String rollNo, String course,
                   String branch, int semester, String email, String phone) {
        this.name     = name;
        this.rollNo   = rollNo;
        this.course   = course;
        this.branch   = branch;
        this.semester = semester;
        this.email    = email;
        this.phone    = phone;
    }

    public Student(int id, String name, String rollNo, String course,
                   String branch, int semester, String email, String phone) {
        this(name, rollNo, course, branch, semester, email, phone);
        this.id = id;
    }

    public int    getId()       { return id; }
    public String getName()     { return name; }
    public String getRollNo()   { return rollNo; }
    public String getCourse()   { return course; }
    public String getBranch()   { return branch; }
    public int    getSemester() { return semester; }
    public String getEmail()    { return email; }
    public String getPhone()    { return phone; }

    public void setId(int id)             { this.id       = id; }
    public void setName(String name)      { this.name     = name; }
    public void setRollNo(String rollNo)  { this.rollNo   = rollNo; }
    public void setCourse(String course)  { this.course   = course; }
    public void setBranch(String branch)  { this.branch   = branch; }
    public void setSemester(int semester) { this.semester = semester; }
    public void setEmail(String email)    { this.email    = email; }
    public void setPhone(String phone)    { this.phone    = phone; }
}