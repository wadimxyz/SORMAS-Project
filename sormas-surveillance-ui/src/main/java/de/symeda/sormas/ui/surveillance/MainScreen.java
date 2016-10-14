package de.symeda.sormas.ui.surveillance;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;

import de.symeda.sormas.ui.surveillance.caze.CasesView;
import de.symeda.sormas.ui.surveillance.task.TasksView;
import de.symeda.sormas.ui.surveillance.user.UsersView;

/**
 * Content of the UI when the user is logged in.
 * 
 * 
 */
@SuppressWarnings("serial")
public class MainScreen extends HorizontalLayout {
    private Menu menu;

    public MainScreen(SurveillanceUI ui) {

        setStyleName("main-screen");

        CssLayout viewContainer = new CssLayout();
        viewContainer.addStyleName("valo-content");
        viewContainer.setSizeFull();

        final Navigator navigator = new Navigator(ui, viewContainer);
        navigator.setErrorView(ErrorView.class);
        
        ControllerProvider.getCaseController().registerViews(navigator);
        
        menu = new Menu(navigator);
        menu.addView(new CasesView(), CasesView.VIEW_NAME, "Cases", FontAwesome.EDIT);
        menu.addView(new TasksView(), TasksView.VIEW_NAME, "Tasks", FontAwesome.TASKS);
        menu.addView(new UsersView(), UsersView.VIEW_NAME, "Officers", FontAwesome.USERS);
        menu.addView(new AboutView(), AboutView.VIEW_NAME, "About", FontAwesome.INFO_CIRCLE);
        
        navigator.addViewChangeListener(viewChangeListener);        
        
        ui.setNavigator(navigator);

        addComponent(menu);
        addComponent(viewContainer);
        setExpandRatio(viewContainer, 1);
        setSizeFull();
    }

    // notify the view menu about view changes so that it can display which view
    // is currently active
    ViewChangeListener viewChangeListener = new ViewChangeListener() {

        @Override
        public boolean beforeViewChange(ViewChangeEvent event) {
        	if (event.getViewName().isEmpty()) {
        		// redirect to default view
        		SurveillanceUI.get().getNavigator().navigateTo(CasesView.VIEW_NAME);
        		return false;
        	}
            return true;
        }

        @Override
        public void afterViewChange(ViewChangeEvent event) {
            menu.setActiveView(event.getViewName());
        }
    };
}
