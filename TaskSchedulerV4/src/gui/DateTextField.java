package gui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class DateTextField extends JTextField {

    private static String DEFAULT_DATE_FORMAT = "MM/dd/yyyy";
    private static final int DIALOG_WIDTH = 200;
    private static final int DIALOG_HEIGHT = 200;

    private SimpleDateFormat dateFormat;
    private DatePanel datePanel = null;
    private JDialog dateDialog = null;

    public DateTextField() {
        this(new Date());
    }

    public DateTextField(String dateFormatPattern, Date date) {
        this(date);
        DEFAULT_DATE_FORMAT = dateFormatPattern;
    }

    public DateTextField(Date date) {
        setDate(date);
        setEditable(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        addListeners();
    }

    private void addListeners() {
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent paramMouseEvent) {
                if (datePanel == null) {
                    datePanel = new DatePanel();
                }
                Point point = getLocationOnScreen();
                point.y = point.y + 30;
                showDateDialog(datePanel, point);
            }
        });
    }

    private void showDateDialog(DatePanel dateChooser, Point position) {
        Frame owner = (Frame) SwingUtilities
                .getWindowAncestor(DateTextField.this);
        if (dateDialog == null || dateDialog.getOwner() != owner) {
            dateDialog = createDateDialog(owner, dateChooser);
        }
        dateDialog.setLocation(getAppropriateLocation(owner, position));
        dateDialog.setVisible(true);
    }

    private JDialog createDateDialog(Frame owner, JPanel contentPanel) {
        JDialog dialog = new JDialog(owner, "Date Selected", true);
        dialog.setUndecorated(true);
        dialog.getContentPane().add(contentPanel, BorderLayout.CENTER);
        dialog.pack();
        dialog.setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
        return dialog;
    }

    private Point getAppropriateLocation(Frame owner, Point position) {
        Point result = new Point(position);
        Point p = owner.getLocation();
        int offsetX = (position.x + DIALOG_WIDTH) - (p.x + owner.getWidth());
        int offsetY = (position.y + DIALOG_HEIGHT) - (p.y + owner.getHeight());

        if (offsetX > 0) {
            result.x -= offsetX;
        }

        if (offsetY > 0) {
            result.y -= offsetY;
        }

        return result;
    }

    private SimpleDateFormat getDefaultDateFormat() {
        if (dateFormat == null) {
            dateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        }
        return dateFormat;
    }

    public void setText(Date date) {
        setDate(date);
    }

    public void setDate(Date date) {
        super.setText(getDefaultDateFormat().format(date));
    }

    public Date getDate() {
        try {
            return getDefaultDateFormat().parse(getText());
        } catch (ParseException e) {
            return new Date();
        }
    }

    private class DatePanel extends JPanel implements ChangeListener {
        int startYear = 1980;
        int lastYear = 2050;

        Color backGroundColor = Color.gray;
        Color palletTableColor = Color.white;
        Color todayBackColor = Color.orange;
        Color weekFontColor = Color.blue;
        Color dateFontColor = Color.black;
        Color weekendFontColor = Color.red;

        Color controlLineColor = Color.pink;
        Color controlTextColor = Color.white;

        JSpinner yearSpin;
        JSpinner monthSpin;
        JButton[][] daysButton = new JButton[6][7];

        DatePanel() {
            setLayout(new BorderLayout());
            setBorder(new LineBorder(backGroundColor, 2));
            setBackground(backGroundColor);

            JPanel topYearAndMonth = createYearAndMonthPanal();
            add(topYearAndMonth, BorderLayout.NORTH);
            JPanel centerWeekAndDay = createWeekAndDayPanal();
            add(centerWeekAndDay, BorderLayout.CENTER);

            reflushWeekAndDay();
        }

        private JPanel createYearAndMonthPanal() {
            Calendar cal = getCalendar();
            int currentYear = cal.get(Calendar.YEAR);
            int currentMonth = cal.get(Calendar.MONTH) + 1;

            JPanel panel = new JPanel();
            panel.setLayout(new FlowLayout());
            panel.setBackground(controlLineColor);

            yearSpin = new JSpinner(new SpinnerNumberModel(currentYear,
                    startYear, lastYear, 1));
            yearSpin.setPreferredSize(new Dimension(56, 20));
            yearSpin.setName("Year");
            yearSpin.setEditor(new JSpinner.NumberEditor(yearSpin, "####"));
            yearSpin.addChangeListener(this);
            panel.add(yearSpin);

            JLabel yearLabel = new JLabel("Year");
            yearLabel.setForeground(controlTextColor);
            panel.add(yearLabel);

            monthSpin = new JSpinner(new SpinnerNumberModel(currentMonth, 1,
                    12, 1));
            monthSpin.setPreferredSize(new Dimension(35, 20));
            monthSpin.setName("Month");
            monthSpin.addChangeListener(this);
            panel.add(monthSpin);

            JLabel monthLabel = new JLabel("Month");
            monthLabel.setForeground(controlTextColor);
            panel.add(monthLabel);

            return panel;
        }

        private JPanel createWeekAndDayPanal() {
            String colname[] = { "S", "M", "T", "W", "T", "F", "S" };
            JPanel panel = new JPanel();
            panel.setFont(new Font("Arial", Font.PLAIN, 10));
            panel.setLayout(new GridLayout(7, 7));
            panel.setBackground(Color.white);

            for (int i = 0; i < 7; i++) {
                JLabel cell = new JLabel(colname[i]);
                cell.setHorizontalAlignment(JLabel.RIGHT);
                if (i == 0 || i == 6) {
                    cell.setForeground(weekendFontColor);
                } else {
                    cell.setForeground(weekFontColor);
                }
                panel.add(cell);
            }

            int actionCommandId = 0;
            for (int i = 0; i < 6; i++)
                for (int j = 0; j < 7; j++) {
                    JButton numBtn = new JButton();
                    numBtn.setBorder(null);
                    numBtn.setHorizontalAlignment(SwingConstants.RIGHT);
                    numBtn.setActionCommand(String
                            .valueOf(actionCommandId));
                    numBtn.setBackground(palletTableColor);
                    numBtn.setForeground(dateFontColor);
                    numBtn.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent event) {
                            JButton source = (JButton) event.getSource();
                            if (source.getText().length() == 0) {
                                return;
                            }
                            dayColorUpdate(true);
                            source.setForeground(todayBackColor);
                            int newDay = Integer.parseInt(source.getText());
                            Calendar cal = getCalendar();
                            cal.set(Calendar.DAY_OF_MONTH, newDay);
                            setDate(cal.getTime());

                            dateDialog.setVisible(false);
                        }
                    });

                    if (j == 0 || j == 6)
                        numBtn.setForeground(weekendFontColor);
                    else
                        numBtn.setForeground(dateFontColor);
                    daysButton[i][j] = numBtn;
                    panel.add(numBtn);
                    actionCommandId++;
                }

            return panel;
        }

        private Calendar getCalendar() {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(getDate());
            return calendar;
        }

        private int getSelectedYear() {
            return ((Integer) yearSpin.getValue()).intValue();
        }

        private int getSelectedMonth() {
            return ((Integer) monthSpin.getValue()).intValue();
        }

        private void dayColorUpdate(boolean isOldDay) {
            Calendar cal = getCalendar();
            int day = cal.get(Calendar.DAY_OF_MONTH);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            int actionCommandId = day - 2 + cal.get(Calendar.DAY_OF_WEEK);
            int i = actionCommandId / 7;
            int j = actionCommandId % 7;
            if (isOldDay) {
                daysButton[i][j].setForeground(dateFontColor);
            } else {
                daysButton[i][j].setForeground(todayBackColor);
            }
        }

        private void reflushWeekAndDay() {
            Calendar cal = getCalendar();
            cal.set(Calendar.DAY_OF_MONTH, 1);
            int maxDayNo = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
            int dayNo = 2 - cal.get(Calendar.DAY_OF_WEEK);
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 7; j++) {
                    String s = "";
                    if (dayNo >= 1 && dayNo <= maxDayNo) {
                        s = String.valueOf(dayNo);
                    }
                    daysButton[i][j].setText(s);
                    dayNo++;
                }
            }
            dayColorUpdate(false);
        }

        public void stateChanged(ChangeEvent e) {
            dayColorUpdate(true);

            JSpinner source = (JSpinner) e.getSource();
            Calendar cal = getCalendar();
            if (source.getName().equals("Year")) {
                cal.set(Calendar.YEAR, getSelectedYear());
            } else {
                cal.set(Calendar.MONTH, getSelectedMonth() - 1);
            }
            setDate(cal.getTime());
            reflushWeekAndDay();
        }
    }
    
    public static void main(String[] args)
    {
    	JFrame frame = new JFrame();
    	JPanel panel = new JPanel();
    	DateTextField a = new DateTextField();
    	panel.add(a);
    	frame.add(panel);
    	frame.setVisible(true);
    }
}
