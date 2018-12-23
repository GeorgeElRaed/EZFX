package InsideWindows;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import App.App;
import Utils.Constants;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class EntityExplorer extends VBox {

	public Node Chosen = null;
	
	private TreeView<HBox> ItemTree;
	private List<TreeItem<HBox>> Items = new ArrayList<>();
	private TreeItem<HBox> Root = new TreeItem<>(new HBox(new Label("Items")));
	
	private static int ID = 0;
	
	public EntityExplorer() {
		setupTree();
		setupDimensions();
		setBackground(new Background(new BackgroundFill(Color.RED, null, null)));

		

	}

	private void setupTree() {
		Root.getChildren().addAll(Items);
		Root.setExpanded(true);
		ItemTree = new TreeView<>(Root);
		ItemTree.prefHeightProperty().bind(prefHeightProperty());
		ItemTree.prefWidthProperty().bind(prefWidthProperty());

		getChildren().add(ItemTree);
	}

	private void setupDimensions() {
		setTranslateX(0);
		setTranslateY(FirstScene.topBar.getPrefHeight());

		setMinWidth(Constants.WIDTH / 10);
		setPrefWidth(Constants.WIDTH / 6);
		setMaxWidth(Constants.WIDTH / 4);

		setPrefHeight((App.Window.getHeight() - FirstScene.topBar.getPrefHeight()) / 2);
		App.Window.heightProperty().addListener(Change -> {
			setPrefHeight((App.Window.getHeight() - FirstScene.topBar.getPrefHeight()) / 2);
		});
	}

	public void AddSection(String SectionName) {
		TreeItem<HBox> treeSection  = new TreeItem<>(new HBox(new Label(SectionName)));
		treeSection.setExpanded(true);
		Items.add(treeSection);
		Root.getChildren().add(treeSection);
	}

	@SuppressWarnings("deprecation")
	public void AddItem(String string, Image image, Node node, String Section) {
		HBox Line = new HBox();
		Line.getChildren().addAll(new ImageView(image), new Label(string));
		TreeItem<HBox> treeItem = new TreeItem<>(Line);
		treeItem.setExpanded(true);
		find(Section).getChildren().add(treeItem);
		EventHandler<MouseEvent> MouseReleased = event -> {
			if (!FirstScene.canvas.isInside(event.getSceneX(), event.getSceneY())) {
				return;
			}
			Node newNode = null;
			try {
				newNode = node.getClass().newInstance();
				newNode.setId("" + (++ID));
			} catch (Exception e) {
				e.printStackTrace();
			}

			
			FirstScene.canvas.addItem(newNode);
			if (FirstScene.hierarchyTree.selected != null) {
				FirstScene.hierarchyTree.mainBox.getChildren().add(newNode);
				HBox.setHgrow(newNode, Priority.ALWAYS);
			} else {
				newNode.setTranslateX(event.getSceneX());
				newNode.setTranslateY(event.getSceneY());
			}
			FirstScene.hierarchyTree.AddItem(string, image, newNode);

			final Node self = newNode;
			newNode.setOnMousePressed(event2 -> {
				Method[] field = self.getClass().getMethods();
				List<String> strings = new ArrayList<>();
				for (Method field2 : field) {
					strings.add(field2.getName());
				}
				Collections.sort(strings);
				String[] strings2 = new String[strings.size()];
				strings.toArray(strings2);
				FirstScene.attributesPanel.setAttributes(self, strings2);
				Chosen = self;
			});
			newNode.setOnKeyPressed(KeyEvent -> {
				if (KeyEvent.getCode() == KeyCode.DELETE) {
					FirstScene.canvas.deleteItem(self);
					FirstScene.hierarchyTree.DeleteItem(self);
					FirstScene.attributesPanel.setAttributes(null, new String[0]);
				}
			});
		};
		Line.setOnMouseReleased(MouseReleased);
	}

	private TreeItem<HBox> find(String Section) {
		for (TreeItem<HBox> item : Root.getChildren())
			if (((Label) item.getValue().getChildren().get(0)).getText().equals(Section))
				return item;

		return null;
	}

}
