package ui;

import dao.StudentDAO;
import dao.StudentDAOImpl;
import model.Student;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;

public class MainFrame extends JFrame {

    private static final Color C_BG      = new Color(15,  17,  26);
    private static final Color C_SURFACE = new Color(24,  27,  42);
    private static final Color C_ACCENT  = new Color(99, 179, 237);
    private static final Color C_ACCENT2 = new Color(72, 149, 239);
    private static final Color C_TEXT    = new Color(226, 232, 240);
    private static final Color C_MUTED   = new Color(113, 128, 150);
    private static final Color C_SUCCESS = new Color( 72, 199, 142);
    private static final Color C_DANGER  = new Color(252,  92,  92);
    private static final Color C_WARN    = new Color(251, 189,  35);
    private static final Color C_BORDER  = new Color( 45,  55,  72);
    private static final Color C_ROW_ALT = new Color( 20,  23,  36);
    private static final Color C_ROW_SEL = new Color( 49,  76, 115);

    private static final Font F_TITLE     = new Font("Segoe UI", Font.BOLD,  22);
    private static final Font F_SUBTITLE  = new Font("Segoe UI", Font.PLAIN, 12);
    private static final Font F_LABEL     = new Font("Segoe UI", Font.BOLD,  12);
    private static final Font F_INPUT     = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font F_BUTTON    = new Font("Segoe UI", Font.BOLD,  12);
    private static final Font F_TABLE_HDR = new Font("Segoe UI", Font.BOLD,  12);
    private static final Font F_TABLE_CEL = new Font("Segoe UI", Font.PLAIN, 12);
    private static final Font F_STATUS    = new Font("Segoe UI", Font.PLAIN, 11);

    private final StudentDAO dao = new StudentDAOImpl();

    private JTextField tfName, tfRollNo, tfCourse, tfBranch, tfEmail, tfPhone;
    private JSpinner   spSemester;
    private JTextField tfSearch;

    private JTable            table;
    private DefaultTableModel tableModel;

    private JLabel lblStatus;
    private int    selectedId = -1;

    public MainFrame() {
        setTitle("Student Management System — Your University Name");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1200, 720);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);
        getContentPane().setBackground(C_BG);
        setLayout(new BorderLayout());

        add(buildHeader(),    BorderLayout.NORTH);
        add(buildMain(),      BorderLayout.CENTER);
        add(buildStatusBar(), BorderLayout.SOUTH);

        loadTable(dao::getAllStudents);
        setStatus("Ready — " + tableModel.getRowCount() + " student(s) loaded.", C_ACCENT);
    }

    private JPanel buildHeader() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(C_SURFACE);
        p.setBorder(new CompoundBorder(
                new MatteBorder(0, 0, 2, 0, C_ACCENT),
                new EmptyBorder(14, 24, 14, 24)));
        JLabel title = new JLabel("🎓  Student Management System");
        title.setFont(F_TITLE);
        title.setForeground(C_ACCENT);
        JLabel sub = new JLabel("Your University Name · Java + MySQL + Swing + JDBC");
        sub.setFont(F_SUBTITLE);
        sub.setForeground(C_MUTED);
        JPanel left = new JPanel(new GridLayout(2, 1, 0, 2));
        left.setOpaque(false);
        left.add(title);
        left.add(sub);
        p.add(left, BorderLayout.WEST);
        return p;
    }

    private JSplitPane buildMain() {
        JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                buildFormPanel(), buildTablePanel());
        sp.setDividerLocation(340);
        sp.setDividerSize(4);
        sp.setBackground(C_BG);
        sp.setBorder(null);
        return sp;
    }

    private JPanel buildFormPanel() {
        JPanel outer = new JPanel(new BorderLayout());
        outer.setBackground(C_BG);
        outer.setBorder(new EmptyBorder(16, 16, 16, 8));
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(C_SURFACE);
        card.setBorder(new CompoundBorder(
                new LineBorder(C_BORDER, 1, true),
                new EmptyBorder(20, 20, 20, 20)));
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(4, 0, 4, 0);
        g.gridx = 0; g.weightx = 1;
        g.gridy = 0;
        JLabel sec = new JLabel("STUDENT DETAILS");
        sec.setFont(new Font("Segoe UI", Font.BOLD, 10));
        sec.setForeground(C_MUTED);
        card.add(sec, g);
        tfName   = field("Full Name");
        tfRollNo = field("e.g. GU2024001");
        tfCourse = field("e.g. B.Tech");
        tfBranch = field("e.g. Computer Science");
        tfEmail  = field("student@galgotias.edu");
        tfPhone  = field("10-digit number");
        addRow(card, g,  1, "Full Name",   tfName);
        addRow(card, g,  3, "Roll Number", tfRollNo);
        addRow(card, g,  5, "Course",      tfCourse);
        addRow(card, g,  7, "Branch",      tfBranch);
        g.gridy = 9;  card.add(lbl("Semester (1–8)"), g);
        spSemester = new JSpinner(new SpinnerNumberModel(1, 1, 8, 1));
        styleSpinner(spSemester);
        g.gridy = 10; card.add(spSemester, g);
        addRow(card, g, 11, "Email", tfEmail);
        addRow(card, g, 13, "Phone", tfPhone);
        g.gridy = 15; g.weighty = 1;
        card.add(Box.createVerticalGlue(), g);
        g.weighty = 0;
        g.gridy = 16;
        card.add(buildButtons(), g);
        outer.add(card, BorderLayout.CENTER);
        return outer;
    }

    private void addRow(JPanel p, GridBagConstraints g,
                        int row, String labelText, JComponent field) {
        g.gridy = row;     p.add(lbl(labelText), g);
        g.gridy = row + 1; p.add(field, g);
    }

    private JPanel buildButtons() {
        JPanel p = new JPanel(new GridLayout(2, 2, 8, 8));
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(12, 0, 0, 0));
        JButton btnAdd    = btn("➕ Add",    C_SUCCESS);
        JButton btnUpdate = btn("✏️ Update", C_ACCENT2);
        JButton btnDelete = btn("🗑 Delete", C_DANGER);
        JButton btnClear  = btn("✖ Clear",  C_MUTED);
        btnAdd   .addActionListener(e -> addStudent());
        btnUpdate.addActionListener(e -> updateStudent());
        btnDelete.addActionListener(e -> deleteStudent());
        btnClear .addActionListener(e -> clearForm());
        p.add(btnAdd); p.add(btnUpdate);
        p.add(btnDelete); p.add(btnClear);
        return p;
    }

    private JPanel buildTablePanel() {
        JPanel outer = new JPanel(new BorderLayout());
        outer.setBackground(C_BG);
        outer.setBorder(new EmptyBorder(16, 8, 16, 16));
        JPanel bar = new JPanel(new BorderLayout(8, 0));
        bar.setOpaque(false);
        bar.setBorder(new EmptyBorder(0, 0, 10, 0));
        tfSearch = field("Search by name or roll number…");
        JButton bSearch = btn("🔍 Search", C_ACCENT2);
        JButton bAll    = btn("↺ All",     C_MUTED);
        bSearch.addActionListener(e -> doSearch());
        bAll.addActionListener(e -> {
            tfSearch.setText("");
            loadTable(dao::getAllStudents);
            setStatus("Showing all students.", C_ACCENT);
        });
        tfSearch.addActionListener(e -> doSearch());
        JPanel bg = new JPanel(new GridLayout(1, 2, 6, 0));
        bg.setOpaque(false);
        bg.add(bSearch);
        bg.add(bAll);
        bar.add(tfSearch, BorderLayout.CENTER);
        bar.add(bg,       BorderLayout.EAST);
        String[] cols = {"ID","Name","Roll No","Course","Branch","Sem","Email","Phone"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        styleTable();
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) fillFormFromTable();
        });
        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(C_BG);
        scroll.setBorder(new LineBorder(C_BORDER, 1, true));
        outer.add(bar,    BorderLayout.NORTH);
        outer.add(scroll, BorderLayout.CENTER);
        return outer;
    }

    private JPanel buildStatusBar() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(C_SURFACE);
        p.setBorder(new CompoundBorder(
                new MatteBorder(1, 0, 0, 0, C_BORDER),
                new EmptyBorder(6, 16, 6, 16)));
        lblStatus = new JLabel("Initialising…");
        lblStatus.setFont(F_STATUS);
        lblStatus.setForeground(C_MUTED);
        JLabel copy = new JLabel("Student Management System © Your University Name");
        copy.setFont(F_STATUS);
        copy.setForeground(C_MUTED);
        p.add(lblStatus, BorderLayout.WEST);
        p.add(copy,      BorderLayout.EAST);
        return p;
    }

    private void addStudent() {
        if (!isFormValid()) return;
        try {
            int id = dao.addStudent(fromForm());
            loadTable(dao::getAllStudents);
            clearForm();
            setStatus("✅ Student added (ID: " + id + ").", C_SUCCESS);
        } catch (SQLException ex) { dbError("adding", ex); }
    }

    private void updateStudent() {
        if (selectedId == -1) { warn("Select a student from the table first."); return; }
        if (!isFormValid()) return;
        try {
            Student s = fromForm();
            s.setId(selectedId);
            dao.updateStudent(s);
            loadTable(dao::getAllStudents);
            clearForm();
            setStatus("✏️ Student updated (ID: " + selectedId + ").", C_ACCENT);
        } catch (SQLException ex) { dbError("updating", ex); }
    }

    private void deleteStudent() {
        if (selectedId == -1) { warn("Select a student from the table first."); return; }
        int c = JOptionPane.showConfirmDialog(this,
                "Delete student ID " + selectedId + "?",
                "Confirm", JOptionPane.YES_NO_OPTION);
        if (c != JOptionPane.YES_OPTION) return;
        try {
            dao.deleteStudent(selectedId);
            loadTable(dao::getAllStudents);
            clearForm();
            setStatus("🗑 Student deleted (ID: " + selectedId + ").", C_DANGER);
        } catch (SQLException ex) { dbError("deleting", ex); }
    }

    private void doSearch() {
        String kw = tfSearch.getText().trim();
        if (kw.isEmpty()) return;
        loadTable(() -> dao.searchStudents(kw));
        setStatus("🔍 " + tableModel.getRowCount() +
                " result(s) for \"" + kw + "\".", C_WARN);
    }

    @FunctionalInterface
    interface Loader { List<Student> load() throws SQLException; }

    private void loadTable(Loader loader) {
        tableModel.setRowCount(0);
        try {
            for (Student s : loader.load())
                tableModel.addRow(new Object[]{
                        s.getId(), s.getName(), s.getRollNo(), s.getCourse(),
                        s.getBranch(), s.getSemester(), s.getEmail(), s.getPhone()});
        } catch (SQLException ex) {
            setStatus("❌ DB error: " + ex.getMessage(), C_DANGER);
        }
    }

    private void fillFormFromTable() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        selectedId = (int) tableModel.getValueAt(row, 0);
        tfName    .setText((String) tableModel.getValueAt(row, 1));
        tfRollNo  .setText((String) tableModel.getValueAt(row, 2));
        tfCourse  .setText((String) tableModel.getValueAt(row, 3));
        tfBranch  .setText((String) tableModel.getValueAt(row, 4));
        spSemester.setValue(        tableModel.getValueAt(row, 5));
        tfEmail   .setText((String) tableModel.getValueAt(row, 6));
        tfPhone   .setText((String) tableModel.getValueAt(row, 7));
        setStatus("Selected ID: " + selectedId +
                " — edit fields then click Update.", C_ACCENT);
    }

    private Student fromForm() {
        return new Student(
                tfName  .getText().trim(), tfRollNo.getText().trim(),
                tfCourse.getText().trim(), tfBranch.getText().trim(),
                (int) spSemester.getValue(),
                tfEmail.getText().trim(),  tfPhone.getText().trim());
    }

    private boolean isFormValid() {
        if (tfName  .getText().isBlank()) { warn("Full Name is required.");   tfName  .requestFocus(); return false; }
        if (tfRollNo.getText().isBlank()) { warn("Roll Number is required."); tfRollNo.requestFocus(); return false; }
        if (tfCourse.getText().isBlank()) { warn("Course is required.");      tfCourse.requestFocus(); return false; }
        if (tfBranch.getText().isBlank()) { warn("Branch is required.");      tfBranch.requestFocus(); return false; }
        if (tfEmail .getText().isBlank()) { warn("Email is required.");       tfEmail .requestFocus(); return false; }
        if (!tfEmail.getText().contains("@")) { warn("Enter a valid email."); tfEmail .requestFocus(); return false; }
        return true;
    }

    private void clearForm() {
        tfName.setText(""); tfRollNo.setText(""); tfCourse.setText("");
        tfBranch.setText(""); tfEmail.setText(""); tfPhone.setText("");
        spSemester.setValue(1);
        selectedId = -1;
        table.clearSelection();
    }

    private void setStatus(String msg, Color c) {
        lblStatus.setText(msg); lblStatus.setForeground(c);
    }

    private void warn(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Warning",
                JOptionPane.WARNING_MESSAGE);
    }

    private void dbError(String action, SQLException ex) {
        String m = ex.getMessage();
        if (m.contains("Duplicate") && m.contains("roll_no"))
            m = "Roll Number already exists.";
        else if (m.contains("Duplicate") && m.contains("email"))
            m = "Email already exists.";
        setStatus("❌ Error " + action + ": " + m, C_DANGER);
        JOptionPane.showMessageDialog(this, m, "DB Error",
                JOptionPane.ERROR_MESSAGE);
    }

    private JLabel lbl(String t) {
        JLabel l = new JLabel(t);
        l.setFont(F_LABEL); l.setForeground(C_TEXT);
        l.setBorder(new EmptyBorder(6, 0, 0, 0));
        return l;
    }

    private JTextField field(String hint) {
        JTextField tf = new JTextField() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() && !isFocusOwner()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setColor(C_MUTED);
                    g2.setFont(getFont().deriveFont(Font.ITALIC));
                    g2.drawString(hint, 8, getHeight() / 2 + 5);
                    g2.dispose();
                }
            }
        };
        tf.setFont(F_INPUT); tf.setBackground(C_BG);
        tf.setForeground(C_TEXT); tf.setCaretColor(C_ACCENT);
        tf.setBorder(new CompoundBorder(
                new LineBorder(C_BORDER, 1, true),
                new EmptyBorder(6, 8, 6, 8)));
        tf.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                tf.setBorder(new CompoundBorder(
                        new LineBorder(C_ACCENT, 1, true),
                        new EmptyBorder(6, 8, 6, 8)));
            }
            @Override public void focusLost(FocusEvent e) {
                tf.setBorder(new CompoundBorder(
                        new LineBorder(C_BORDER, 1, true),
                        new EmptyBorder(6, 8, 6, 8)));
            }
        });
        return tf;
    }

    private void styleSpinner(JSpinner sp) {
        sp.setFont(F_INPUT); sp.setBackground(C_BG); sp.setForeground(C_TEXT);
        sp.setBorder(new CompoundBorder(
                new LineBorder(C_BORDER, 1, true),
                new EmptyBorder(4, 6, 4, 6)));
        JComponent ed = sp.getEditor();
        if (ed instanceof JSpinner.DefaultEditor de) {
            de.getTextField().setBackground(C_BG);
            de.getTextField().setForeground(C_TEXT);
            de.getTextField().setFont(F_INPUT);
            de.getTextField().setCaretColor(C_ACCENT);
        }
    }

    private JButton btn(String text, Color base) {
        JButton b = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = getModel().isPressed()  ? base.darker().darker() :
                        getModel().isRollover() ? base.brighter() : base;
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setFont(F_BUTTON); b.setForeground(Color.WHITE);
        b.setContentAreaFilled(false); b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(100, 34));
        return b;
    }

    private void styleTable() {
        table.setFont(F_TABLE_CEL); table.setBackground(C_BG);
        table.setForeground(C_TEXT); table.setGridColor(C_BORDER);
        table.setRowHeight(32);
        table.setSelectionBackground(C_ROW_SEL);
        table.setSelectionForeground(Color.WHITE);
        table.setShowVerticalLines(false);
        table.setFillsViewportHeight(true);
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(
                    JTable t, Object v, boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                setBackground(sel ? C_ROW_SEL : row % 2 == 0 ? C_BG : C_ROW_ALT);
                setForeground(sel ? Color.WHITE : C_TEXT);
                setBorder(new EmptyBorder(0, 8, 0, 8));
                setFont(F_TABLE_CEL);
                return this;
            }
        });
        JTableHeader h = table.getTableHeader();
        h.setBackground(C_SURFACE); h.setForeground(C_ACCENT);
        h.setFont(F_TABLE_HDR);
        h.setBorder(new MatteBorder(0, 0, 2, 0, C_ACCENT));
        h.setReorderingAllowed(false);
        int[] w = {40,160,100,90,130,50,200,110};
        for (int i = 0; i < w.length; i++)
            table.getColumnModel().getColumn(i).setPreferredWidth(w[i]);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(
                    UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {}
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}