package schedule.view;

import schedule.model.Resource;
import schedule.model.ScheduleModel;
import schedule.model.Task;

import java.awt.*;
import java.util.List;

class ResourcePanel<R extends Resource> extends PanelWithRows {
    private final ScheduleModel<R, ? extends Task> model;
    ResourceRenderer<R> resourceRenderer = new ResourceRenderer.Default<>();

    public ResourcePanel(RowHighlightTracker rowHighlightTracker, ScheduleModel<R, ? extends Task> model, ScheduleView.Configuration configuration) {
        super(configuration, rowHighlightTracker);
        this.model = model;
    }

    @Override
    protected void paintComponent(Graphics g) {
        List<R> resources = model.getResources();
        for (int i = 0; i < resources.size(); i++) {
            renderRow(g, i, resources.get(i));
        }
    }

    private void renderRow(Graphics g, int rowNumber, R resource) {
        renderRowBackground(g, rowNumber);
        Component renderingComponent = resourceRenderer.getRenderingComponent(resource);
        int y = rowNumber * configuration.getRowHeightWithMargins() + configuration.rowMargin;
        renderingComponent.setSize(new Dimension(getWidth(), configuration.rowHeight));
        renderingComponent.doLayout();
        renderingComponent.paint(g.create(0, y, getWidth(), configuration.rowHeight));

    }
}
