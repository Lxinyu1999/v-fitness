package com.group77.fitness.fitnesscore.controller.common;

import com.group77.fitness.fitnesscore.dao.GymDAO;
import com.group77.fitness.fitnesscore.dao.SearchDAO;
import com.group77.fitness.fitnesscore.dao.impl.GymDAOImpl;
import com.group77.fitness.fitnesscore.dao.impl.SearchDAOImpl;
import com.group77.fitness.fitnesscore.vo.Account;
import com.group77.fitness.fitnesscore.vo.Member;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.io.IOException;


public class TopBarController {
    @FXML private Label labelGymName;
    @FXML private ImageView imageViewAvatar;
    @FXML private TextField textFieldSearch;
    @FXML private Label labelAccountID;
    @FXML private Label labelMembershipType;

    private final GymDAO gymDAO = new GymDAOImpl();
    private final SearchDAO searchDAO = new SearchDAOImpl();

    private TabPane tabPane;

    public TopBarController() throws Exception {}

    @FXML
    public void initialize() {
        this.textFieldSearch.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                try {
                    this.buttonSearchMouseClicked();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Account loadDataUpdateUI(TabPane tabPane) {
        this.tabPane = tabPane;

        this.labelGymName.setText(this.gymDAO.getGym().getGymName());

        // getScene() will always return null if invoked in initialize()
        Stage stage = (Stage) this.labelAccountID.getScene().getWindow();
        Account account = (Account) stage.getUserData();
        this.labelAccountID.setText(account.getAccountID());
        this.imageViewAvatar.setImage(new Image("file:///" + account.getAvatarAbsolutePath()));

        if (account instanceof Member) {
            Member member = (Member) account;
            this.labelMembershipType.setText(member.getMembershipType());
        }
        else {
            this.labelMembershipType.setVisible(false);
        }
        return account;
    }

    @FXML
    public void buttonSearchMouseClicked() throws IOException {
        boolean hasSearchResultsTab = false;

        ObservableList<Tab> tabs = this.tabPane.getTabs();

        for (Tab tab : tabs) {
            if (tab.getText().equals("Search Results")) {
                hasSearchResultsTab = true;
                break;
            }
        }

        Node rootNode;
        if (!hasSearchResultsTab) {
            rootNode = FXMLLoader.load(getClass().getResource("/fxml/common/SearchResult.fxml"));

            Tab tab = new Tab("Search Results");
            tab.setContent(rootNode);
            this.tabPane.getTabs().add(tab);
        }
        else {
            rootNode = tabs.get(tabs.size() - 1).getContent();
        }

        ListView<String> listView = (ListView<String>) rootNode.lookup("#listViewSearchResult");
        listView.getItems().clear();
        listView.getItems().addAll(this.searchDAO.searchByKeyword(this.textFieldSearch.getText()));

        this.tabPane.getSelectionModel().selectLast();
        this.tabPane.requestFocus();
        this.textFieldSearch.requestFocus();
    }
}
