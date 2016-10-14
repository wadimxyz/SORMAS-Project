package de.symeda.sormas.ui.surveillance;

import de.symeda.sormas.ui.surveillance.caze.CaseController;
import de.symeda.sormas.ui.surveillance.person.PersonController;
import de.symeda.sormas.ui.surveillance.task.TaskController;
import de.symeda.sormas.ui.surveillance.user.UserController;
import de.symeda.sormas.ui.utils.BaseControllerProvider;

/**
 * @author Stefan Szczesny
 */
public class ControllerProvider extends BaseControllerProvider {

	private final CaseController caseController;
	private final PersonController personController;
	private final UserController userController;
	private final TaskController taskController;

	public ControllerProvider() {
		super();

		caseController = new CaseController();
		personController = new PersonController();
		userController = new UserController();
		taskController = new TaskController();
	}

	protected static ControllerProvider get() {
		return (ControllerProvider) BaseControllerProvider.get();
	}

	public static CaseController getCaseController() {
		return get().caseController;
	}

	public static PersonController getPersonController() {
		return get().personController;
	}
	
	public static UserController getUserController() {
		return get().userController;
	}

	public static TaskController getTaskController() {
		return get().taskController;
	}
}
