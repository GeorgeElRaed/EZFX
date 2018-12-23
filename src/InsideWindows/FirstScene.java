package InsideWindows;

import App.App;
import SaveLoadTree.TreeWriter;
import Utils.UserData;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;

public class FirstScene extends Scene {

	public static TreeWriter treeWriter;
	public static TopBar topBar;
	public static EntityExplorer entityExplorer;
	public static AttributesPanel attributesPanel;
	public static Canvas canvas;
	public static HierarchyTree hierarchyTree;
	public static Group TheFirstOne = new Group();

	public FirstScene() {
		super(TheFirstOne, App.Window.getWidth(), App.Window.getHeight());
		setFill(Color.rgb(40, 40, 40));
		treeWriter = new TreeWriter(UserData.PATH);
		topBar = new TopBar(12);
		entityExplorer = new EntityExplorer();
		hierarchyTree = new HierarchyTree();
		attributesPanel = new AttributesPanel();
		canvas = new Canvas(40, 40);
		TheFirstOne.getChildren().addAll(topBar, entityExplorer, hierarchyTree , attributesPanel, canvas);

	}

}
