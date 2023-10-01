package com.group77.fitness.fitnesscore.controller.member;

import com.group77.fitness.fitnesscore.controller.GUIScheduler;
import com.group77.fitness.fitnesscore.dao.GymDAO;
import com.group77.fitness.fitnesscore.dao.SessionDAO;
import com.group77.fitness.fitnesscore.dao.impl.GymDAOImpl;
import com.group77.fitness.fitnesscore.dao.impl.SessionDAOImpl;
import com.group77.fitness.fitnesscore.util.NotificationType;
import com.group77.fitness.fitnesscore.vo.Member;
import com.group77.fitness.fitnesscore.vo.Session;
import com.group77.fitness.fitnesscore.vo.Trainer;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;

public class TrainerDetailController {
    @FXML private ImageView imageViewAvatar;
    @FXML private Label labelID;
    @FXML private Label labelGender;
    @FXML private Label labelAge;
    @FXML private TextArea textAreaTeachingSubjects;

    @FXML private AnchorPane anchorPaneLiveSession;
    @FXML private ChoiceBox<String> choiceBoxSessionSubject;
    @FXML private DatePicker datePickerSessionDate;
    @FXML private Spinner<Integer> spinnerStartTimeHour;
    @FXML private Spinner<Integer> spinnerStartTimeMinute;
    @FXML private Spinner<Integer> spinnerEndTimeHour;
    @FXML private Spinner<Integer> spinnerEndTimeMinute;
    @FXML private Label labelWarningSessionSubject;
    @FXML private Label labelWarningSessionTime;
    @FXML protected AnchorPane anchorPaneTimeLabel;
    @FXML private AnchorPane anchorPaneTimeLine;
    @FXML private HBox hBoxTimelineScale;
    @FXML private Button buttonBook;

    private final LocalDate curDate = LocalDate.now();
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    private final GymDAO gymDAO = new GymDAOImpl();
    private final SessionDAO sessionDAO = new SessionDAOImpl();

    private Trainer trainer;

    public TrainerDetailController() throws Exception {}

    @FXML public void initialize() {

        this.choiceBoxSessionSubject.getSelectionModel().selectedIndexProperty().addListener((observableValue, number, t1) -> {
            this.labelWarningSessionSubject.setText("");    // Reset the warning once an item is chosen
        });

        // ----------------------------------- Initialize the DatePicker -----------------------------------
        // ---------- Another way to get the current date by calendar ----------
        // Calendar calendar = Calendar.getInstance();
        // Calendar.MONTH starts from 0 rather than 1, thereby plus 1
        // int year = calendar.get(Calendar.YEAR), month = calendar.get(Calendar.MONTH) + 1, day = calendar.get(Calendar.DAY_OF_MONTH);
        // this.datePickerSessionDate.setValue(LocalDate.of(year, month, day));

        this.datePickerSessionDate.setValue(this.curDate);

        // refreshBookingsToTimeLine() can be invoked only after invoking loadDataUpdateUI() (loadTrainerInfo()),
        // because refreshBookingsToTimeLine() needs the trainer's info, which is loaded by loadTrainerInfo() in
        // loadDataUpdateUI(). Thus, refreshBookingsToTimeLine() can't be invoked in initialize()
        // IT HAS BEEN MOVED TO loadLiveSessionInfo()
        // this.refreshBookingsToTimeLine(this.curDate.getYear(), this.curDate.getMonthValue(), this.curDate.getDayOfMonth());

        LocalDate maxDate = this.curDate.plusYears(1);

        // Disable the past dates and the dates over one year
        // https://stackoverflow.com/questions/48238855/how-to-disable-past-dates-in-datepicker-of-javafx-scene-builder
        this.datePickerSessionDate.setDayCellFactory(datePicker -> new DateCell() {
            @Override
            public void updateItem(LocalDate localDate, boolean empty) {
                super.updateItem(localDate, empty);
                // Do not split this code into two, only the second one will take effects
                setDisable(empty || localDate.compareTo(curDate) < 0 || localDate.compareTo(maxDate) > 0);
            }
        });

        this.datePickerSessionDate.valueProperty().addListener((observableValue, oldValue, newValue) ->
        {
            this.labelWarningSessionTime.setText("");   // Reset the warning
            try {
                this.refreshBookingsToTimeLine(newValue.getYear(), newValue.getMonthValue(), newValue.getDayOfMonth());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });

        // ----------------------------------- Initialize the Spinners -----------------------------------
        // Control the range:
        this.spinnerStartTimeHour.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 8));
        this.spinnerStartTimeMinute.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0));
        this.spinnerEndTimeHour.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 9));
        this.spinnerEndTimeMinute.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0));

        // Add listeners to deal with JavaFX Spinner empty text NPE,
        // details at https://stackoverflow.com/questions/36549829/javafx-spinner-empty-text-nullpointerexception
        this.spinnerStartTimeHour.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            try {
                if (newValue == null) this.spinnerStartTimeHour.getValueFactory().setValue(oldValue);
            }
            catch (Exception ignored) {}
        });

        this.spinnerStartTimeMinute.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            try {
                if (newValue == null) this.spinnerStartTimeMinute.getValueFactory().setValue(oldValue);
            }
            catch (Exception ignored) {}
        });

        this.spinnerEndTimeHour.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            try {
                if (newValue == null) this.spinnerEndTimeHour.getValueFactory().setValue(oldValue);
            }
            catch (Exception ignored) {}
        });

        this.spinnerEndTimeMinute.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            try {
                if (newValue == null) this.spinnerEndTimeMinute.getValueFactory().setValue(oldValue);
            }
            catch (Exception ignored) {}
        });
    }


    public void loadDataUpdateUI() throws Exception {
        this.loadTrainerInfo();
        this.loadLiveSessionInfo();
    }

    private void loadTrainerInfo() {
        // Note: Unable to get the container outside the ImageView by .getScene().lookup("#containerID")
        // because .getScene() always return null here. TrainerDetail is a new tab's content, before
        // the new tab is actually added to the window, TrainerDetail has no Scene and Stage
        this.trainer = (Trainer) this.imageViewAvatar.getParent().getUserData();
        this.imageViewAvatar.setImage(new Image("file:///" + this.trainer.getAvatarAbsolutePath()));
        this.labelID.setText(this.trainer.getAccountID());
        this.labelGender.setText(this.trainer.getGender());
        this.labelAge.setText(String.valueOf(this.trainer.getAge()));
        this.textAreaTeachingSubjects.setText(String.join(", ", this.trainer.getTeachingSubjects()));
    }

    // ------------------------------- Book a Live Session -------------------------------
    private void addBlockToTimeline(String startTime, String endTime, boolean isAvailable) throws ParseException {  // ParseException from simpleDateFormat.parse()
        Calendar calStartTime = Calendar.getInstance();
        calStartTime.setTime(this.timeFormat.parse(startTime));
        Calendar calEndTime = Calendar.getInstance();
        calEndTime.setTime(this.timeFormat.parse(endTime));

        long timeDelta = calEndTime.getTimeInMillis() - calStartTime.getTimeInMillis();
        double widthRate = timeDelta * 1.0 / 60000 / 1440;

        double startPositionRate = (calStartTime.get(Calendar.HOUR_OF_DAY) * 60 +
                calStartTime.get(Calendar.MINUTE)) * 1.0 / 1440;

        // Should use getPrefHeight() and getPrefWidth() rather than getHeight() and getWidth(), since
        // getHeight() and getWidth() return 0
        // Constructor's params: (X, Y, width, height)
        // Note that X and Y are different from LayoutX and LayoutY
        Rectangle rectangle = new Rectangle(this.anchorPaneTimeLine.getPrefWidth() * startPositionRate, 0,
                this.anchorPaneTimeLine.getPrefWidth() * widthRate, this.anchorPaneTimeLine.getPrefHeight());

        // setLayoutX and setX have the same effect, but note that should use getter accordingly.
//        Rectangle rectangle = new Rectangle();
//        rectangle.setLayoutX(this.anchorPaneTimeLine.getPrefWidth() * startPositionRate);
//        rectangle.setWidth(this.anchorPaneTimeLine.getPrefWidth() * widthRate);
//        rectangle.setHeight(this.anchorPaneTimeLine.getPrefHeight());


        if (isAvailable) { rectangle.setFill(Color.LIGHTGREEN); }
        else { rectangle.setFill(Color.LIGHTPINK); }

        this.anchorPaneTimeLine.getChildren().add(rectangle);

        // ------------------------- Add two labels to display the start/end time -------------------------
        Label labelStartTime = new Label(startTime);
        // Before the label is actually added to something, .getWidth() etc. return 0
        // Use rectangle.getX() instead of rectangle.getLayoutX()
        labelStartTime.setPrefWidth(33);
//        labelStartTime.setLayoutX(rectangle.getX() - 14);
        labelStartTime.setLayoutX(rectangle.getX());
        labelStartTime.setFont(new Font(10));

        Label labelEndTime = new Label(endTime);
        labelEndTime.setPrefWidth(33);
        labelEndTime.setLayoutX(rectangle.getX() + rectangle.getWidth() - labelEndTime.getPrefWidth() + 6);
        labelEndTime.setFont(new Font(10));


        this.anchorPaneTimeLabel.getChildren().addAll(labelStartTime, labelEndTime);
    }

    private void loadLiveSessionInfo() throws ParseException {  // ParseException from refreshBookingsToTimeLine()
        ArrayList<String> teachingSubjects = this.trainer.getTeachingSubjects();
        for (String subject : teachingSubjects) {
            this.choiceBoxSessionSubject.getItems().add(subject);
        }

        this.refreshBookingsToTimeLine(this.curDate.getYear(), this.curDate.getMonthValue(), this.curDate.getDayOfMonth());

        // Create scales under the timeline
        for (int i = 0; i <= 24 ; i++) {
            Label labelHour = new Label(String.format("%02d", i));
            labelHour.setFont(new Font(10));
            labelHour.setPrefWidth(15);     // set a uniform width
            this.hBoxTimelineScale.getChildren().add(labelHour);
        }

        this.hBoxTimelineScale.setSpacing((this.hBoxTimelineScale.getPrefWidth() - 15*24) / 24);
    }

    private void refreshBookingsToTimeLine(int year, int month, int day) throws ParseException {
        // ---------------- Return to the original state (remove original bookings) -------------------------
        this.anchorPaneTimeLine.getChildren().clear();
        this.anchorPaneTimeLabel.getChildren().clear();
        ArrayList<String> trainerWorkingHours = this.gymDAO.getGym().getTrainerWorkingHours();
        for (int i = 0; i < trainerWorkingHours.size(); i += 2) {
            this.addBlockToTimeline(trainerWorkingHours.get(i), trainerWorkingHours.get(i+1), true);
        }

        // ----------------------------- load the sessions of the trainer on the chosen date -----------------------------
        ArrayList<Session> resSessions = this.sessionDAO.getSessionsByDateAndTrainerID(year, month, day, this.trainer.getAccountID());

        if (resSessions.size() == 0) return;

        for (Session session : resSessions) {
            String startTime = session.getSessionInterval()[0];
            String endTime = session.getSessionInterval()[1];
            this.addBlockToTimeline(startTime, endTime, false);
        }
    }

    public void buttonBookMouseClicked() throws ParseException, IOException {
        this.labelWarningSessionSubject.setText("");
        this.labelWarningSessionTime.setText("");

        // Check that one of the session subjects has been chosen
        if (this.choiceBoxSessionSubject.getValue() == null) {
            this.labelWarningSessionSubject.setText("Please select one item");
            return;
        }

        // --------------------------------- Check whether the time range is valid ---------------------------------
        ArrayList<String> trainerWorkingHours = this.gymDAO.getGym().getTrainerWorkingHours();

        int sessionStartTimeHour = this.spinnerStartTimeHour.getValue();
        int sessionStartTimeMinute = this.spinnerStartTimeMinute.getValue();
        int sessionEndTimeHour = this.spinnerEndTimeHour.getValue();
        int sessionEndTimeMinute = this.spinnerEndTimeMinute.getValue();

        Calendar sessionStartTime = Calendar.getInstance();
        sessionStartTime.setTime(this.timeFormat.parse(sessionStartTimeHour + ":" + sessionStartTimeMinute));
        Calendar sessionEndTime = Calendar.getInstance();
        sessionEndTime.setTime(this.timeFormat.parse(sessionEndTimeHour + ":" + sessionEndTimeMinute));

        // Make sure the session lasts for at least 30 minutes
        int durationMinute = (sessionEndTime.get(Calendar.HOUR_OF_DAY) - sessionStartTime.get(Calendar.HOUR_OF_DAY)) * 60 +
                (sessionEndTime.get(Calendar.MINUTE) - sessionStartTime.get(Calendar.MINUTE));
        if (durationMinute < 30) {
            this.labelWarningSessionTime.setText("The live session should last for at least 30 minutes.");
            return;
        }

        // Check that whether the start/end time is in valid range (the trainer working hours)
        Calendar workStartTime = Calendar.getInstance();
        Calendar workEndTime = Calendar.getInstance();
        boolean isValidRange = false;
        for (int i = 0; i < trainerWorkingHours.size(); i += 2) {
            workStartTime.setTime(this.timeFormat.parse(trainerWorkingHours.get(i)));
            workEndTime.setTime(this.timeFormat.parse(trainerWorkingHours.get(i+1)));

            // if the session time is in one of the working time ranges
            if (sessionStartTime.compareTo(workStartTime) >= 0 && sessionEndTime.compareTo(workEndTime) <= 0) {
                isValidRange = true;
                break;
            }
        }
        if (!isValidRange) {
            this.labelWarningSessionTime.setText("Not in trainers' working time.");
            return;
        }

        // Check that whether the start/end time is conflict with those reserved ones of the trainer
        // (This also includes the conflict with existing sessions with the trainer of the current member)
        LocalDate chosenDate = this.datePickerSessionDate.getValue();
        ArrayList<Session> reservedSessions = this.sessionDAO.getSessionsByDateAndTrainerID(chosenDate.getYear(),
                chosenDate.getMonthValue(), chosenDate.getDayOfMonth(), this.trainer.getAccountID());
        Calendar reservedStartTime = Calendar.getInstance();
        Calendar reservedEndTime = Calendar.getInstance();
        for (Session session : reservedSessions) {
            reservedStartTime.setTime(this.timeFormat.parse(session.getSessionInterval()[0]));
            reservedEndTime.setTime(this.timeFormat.parse(session.getSessionInterval()[1]));

            // if the input session time has intersection with the reserved ones on the chosen data
            if (!(reservedEndTime.compareTo(sessionStartTime) <= 0 || sessionEndTime.compareTo(reservedStartTime) <= 0)) {
                this.labelWarningSessionTime.setText("Conflicts with reserved sessions.");
                return;
            }
        }

        // Check that whether the start/end time is conflict with his/her existing bookings of other trainers
        Stage stage = (Stage) this.buttonBook.getScene().getWindow();
        Member curMember = (Member) stage.getUserData();    // Get the Member object from the stage's user data
        ArrayList<Session> memberSessions = this.sessionDAO.getSessionsByDateAndMemberID(chosenDate.getYear(),
                chosenDate.getMonthValue(), chosenDate.getDayOfMonth(), curMember.getAccountID());
        Calendar selfExistingStartTime = Calendar.getInstance();
        Calendar selfExistingEndTime = Calendar.getInstance();
        for (Session session : memberSessions) {
            selfExistingStartTime.setTime(this.timeFormat.parse(session.getSessionInterval()[0]));
            selfExistingEndTime.setTime(this.timeFormat.parse(session.getSessionInterval()[1]));

            if (!(selfExistingEndTime.compareTo(sessionStartTime) <= 0 || sessionEndTime.compareTo(selfExistingStartTime) <= 0)) {
                this.labelWarningSessionSubject.setText("Conflicts with your existing sessions of other trainer.");
                return;
            }
        }

        // --------------------------------- Add a new session ---------------------------------
        String sessionDate = String.format("%d-%02d-%02d", chosenDate.getYear(), chosenDate.getMonthValue(), chosenDate.getDayOfMonth());
        String[] sessionInterval = {String.format("%02d:%02d", sessionStartTimeHour, sessionStartTimeMinute),
                                    String.format("%02d:%02d", sessionEndTimeHour, sessionEndTimeMinute)};
        Session newSession = new Session(curMember.getAccountID(), this.trainer.getAccountID(), sessionDate,
                sessionInterval, this.choiceBoxSessionSubject.getValue());
        sessionDAO.addSession(newSession);

        if (sessionDAO.addSession(newSession)) {
            new GUIScheduler().createNotificationWindow(this.imageViewAvatar.getScene().getWindow(), NotificationType.SUCCESS, "The booking is successful!");
        }
    }
}
