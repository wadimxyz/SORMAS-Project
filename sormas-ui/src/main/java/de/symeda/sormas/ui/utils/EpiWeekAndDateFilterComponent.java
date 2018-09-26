package de.symeda.sormas.ui.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.themes.ValoTheme;

import de.symeda.sormas.api.caze.NewCaseDateType;
import de.symeda.sormas.api.utils.DateHelper;
import de.symeda.sormas.api.utils.EpiWeek;
import de.symeda.sormas.ui.dashboard.DateFilterOption;

public class EpiWeekAndDateFilterComponent extends HorizontalLayout {

	private static final long serialVersionUID = 8752630393182185034L;

	private ComboBox dateFilterOptionFilter;
	private ComboBox newCaseDateTypeSelector;
	private ComboBox weekFromFilter;
	private ComboBox weekToFilter;
	private PopupDateField dateFromFilter;
	private PopupDateField dateToFilter;

	public EpiWeekAndDateFilterComponent(Button applyButton, boolean fillAutomatically, boolean showCaption, boolean showNewCaseDateTypeSelector) {
		setSpacing(true);

		Calendar c = Calendar.getInstance();
		c.setTime(new Date());

		dateFilterOptionFilter = new ComboBox();
		newCaseDateTypeSelector = new ComboBox();
		weekFromFilter = new ComboBox();
		weekToFilter = new ComboBox();
		dateFromFilter = new PopupDateField();
		dateToFilter = new PopupDateField();

		// Date filter options
		dateFilterOptionFilter.setWidth(200, Unit.PIXELS);
		dateFilterOptionFilter.addItems((Object[])DateFilterOption.values());
		dateFilterOptionFilter.setNullSelectionAllowed(false);
		dateFilterOptionFilter.select(DateFilterOption.EPI_WEEK);
		if (showCaption) {
			CssStyles.style(dateFilterOptionFilter, CssStyles.FORCE_CAPTION);
		}

		dateFilterOptionFilter.addValueChangeListener(e -> {
			if (e.getProperty().getValue() == DateFilterOption.DATE) {
				int newIndex = getComponentIndex(weekFromFilter);
				removeComponent(weekFromFilter);
				removeComponent(weekToFilter);
				addComponent(dateFromFilter, newIndex);
				addComponent(dateToFilter, newIndex + 1);

				if (fillAutomatically) {
					dateFromFilter.setValue(DateHelper.subtractDays(c.getTime(), 7));
				}
				if (fillAutomatically) {
					dateToFilter.setValue(c.getTime());
				}
			} else {
				int newIndex = getComponentIndex(dateFromFilter);
				removeComponent(dateFromFilter);
				removeComponent(dateToFilter);
				addComponent(weekFromFilter, newIndex);
				addComponent(weekToFilter, newIndex + 1);

				if (fillAutomatically) {
					weekFromFilter.setValue(DateHelper.getEpiWeek(c.getTime()));
				}
				if (fillAutomatically) {
					weekToFilter.setValue(DateHelper.getEpiWeek(c.getTime()));
				}
			}
		});
		addComponent(dateFilterOptionFilter);

		// New case date type selector
		if (showNewCaseDateTypeSelector) {
			newCaseDateTypeSelector.setWidth(200, Unit.PIXELS);
			newCaseDateTypeSelector.addItems((Object[]) NewCaseDateType.values());
			newCaseDateTypeSelector.setNullSelectionAllowed(false);
			newCaseDateTypeSelector.select(NewCaseDateType.MOST_RELEVANT);
			if (showCaption) {
				CssStyles.style(newCaseDateTypeSelector, CssStyles.FORCE_CAPTION);
			}
			addComponent(newCaseDateTypeSelector);
			
			Label infoLabel = new Label(FontAwesome.INFO_CIRCLE.getHtml(), ContentMode.HTML);
			infoLabel.setSizeUndefined();
			infoLabel.setDescription("By default, cases are filtered by the most relevant date available:<br/><ul><li>Symptom onset date</li><li>Case reception date</li><li>Case report date</li></ul>"
					+ "This means that, when a case e.g. has a symptom onset date, only this date will be taken into account when searching the list for cases in the specified date range. You can specify a date type "
					+ "in the dropdown menu to instead specifically filter by this date.<br/><br/>"
					+ "<b>Example:</b> Case A has been created this week and therefore has a report date that lies in this week as well. However, Case A also has a symptom onset date that is set to last week (because it has been entered retrospectively). "
					+ "By default, when \"Most relevant date\" is selected and you have set the filter to only display cases of this week, Case A will not appear in the list because its symptom onset date lies in the previous week. "
					+ "For the case to appear in the list, you need to select \"Case report date\" which will result in only the report date being considered when filtering the list.");
			CssStyles.style(infoLabel, CssStyles.LABEL_XLARGE, CssStyles.LABEL_SECONDARY);
			addComponent(infoLabel);
		}

		// Epi week filter
		List<EpiWeek> epiWeekList = DateHelper.createEpiWeekList(c.get(Calendar.YEAR), c.get(Calendar.WEEK_OF_YEAR));

		weekFromFilter.setWidth(200, Unit.PIXELS);
		for (EpiWeek week : epiWeekList) {
			weekFromFilter.addItem(week);
		}
		weekFromFilter.setNullSelectionAllowed(false);
		if (fillAutomatically) {
			weekFromFilter.setValue(DateHelper.getEpiWeek(c.getTime()));
		}
		if (showCaption) {
			weekFromFilter.setCaption("From Epi Week");
		}
		if (applyButton != null) {
			weekFromFilter.addValueChangeListener(e -> {
				applyButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
			});
		}
		addComponent(weekFromFilter);

		weekToFilter.setWidth(200, Unit.PIXELS);
		for (EpiWeek week : epiWeekList) {
			weekToFilter.addItem(week);
		}
		weekToFilter.setNullSelectionAllowed(false);
		if (fillAutomatically) {
			weekToFilter.setValue(DateHelper.getEpiWeek(c.getTime()));
		}
		if (showCaption) {
			weekToFilter.setCaption("To Epi Week");
		}
		if (applyButton != null) {
			weekToFilter.addValueChangeListener(e -> {
				applyButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
			});
		}
		addComponent(weekToFilter);

		// Date filter
		dateFromFilter.setWidth(200, Unit.PIXELS);
		if (showCaption) {
			dateFromFilter.setCaption("From");
		}
		if (applyButton != null) {
			dateFromFilter.addValueChangeListener(e -> {
				applyButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
			});
		}

		dateToFilter.setWidth(200, Unit.PIXELS);
		if (showCaption) {
			dateToFilter.setCaption("To");
		}
		if (applyButton != null) {
			dateToFilter.addValueChangeListener(e -> {
				applyButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
			});
		}
	}

	public ComboBox getDateFilterOptionFilter() {
		return dateFilterOptionFilter;
	}

	public ComboBox getNewCaseDateTypeSelector() {
		return newCaseDateTypeSelector;
	}
	
	public ComboBox getWeekFromFilter() {
		return weekFromFilter;
	}

	public ComboBox getWeekToFilter() {
		return weekToFilter;
	}

	public PopupDateField getDateFromFilter() {
		return dateFromFilter;
	}

	public PopupDateField getDateToFilter() {
		return dateToFilter;
	}

}
