/**
 * @author: clay
 * @data: 2021/5/12 0:05
 * @email: clay@hchyun.com
 * @description: node
 */
export default {
  name: 'top-table',
  extendName: 'table',
  options:{
    draw(cfg, group) {
      //todo 画线
      const edge = group.cfg.item;
      const sourceNode = edge.getSource().getModel();
      const targetNode = edge.getTarget().getModel();

      const sourceIndex = sourceNode.attrs.findIndex(
        (e) => e.key === cfg.sourceKey
      );

      const sourceStartIndex = sourceNode.startIndex || 0;

      let sourceY = 15;

      if (!sourceNode.collapsed && sourceIndex > sourceStartIndex - 1) {
        sourceY = 30 + (sourceIndex - sourceStartIndex + 0.5) * 30;
        sourceY = Math.min(sourceY, 300);
      }

      const targetIndex = targetNode.attrs.findIndex(
        (e) => e.key === cfg.targetKey
      );

      const targetStartIndex = targetNode.startIndex || 0;

      let targetY = 15;

      if (!targetNode.collapsed && targetIndex > targetStartIndex - 1) {
        targetY = (targetIndex - targetStartIndex + 0.5) * 30 + 30;
        targetY = Math.min(targetY, 300);
      }

      const startPoint = {
        ...cfg.startPoint
      };
      const endPoint = {
        ...cfg.endPoint
      };

      startPoint.y = startPoint.y + sourceY;
      endPoint.y = endPoint.y + targetY;

      let shape;
      if (sourceNode.id !== targetNode.id) {
        shape = group.addShape("path", {
          attrs: {
            stroke: "#5B8FF9",
            path: [
              ["M", startPoint.x, startPoint.y],
              [
                "C",
                endPoint.x / 3 + (2 / 3) * startPoint.x,
                startPoint.y,
                endPoint.x / 3 + (2 / 3) * startPoint.x,
                endPoint.y,
                endPoint.x,
                endPoint.y,
              ],
            ],
            endArrow: true,
          },
          name: "path-shape",
        });
      } else if (!sourceNode.collapsed) {
        let gap = Math.abs((startPoint.y - endPoint.y) / 3);
        if (startPoint["index"] === 1) {
          gap = -gap;
        }
        shape = group.addShape("path", {
          attrs: {
            stroke: "#5B8FF9",
            path: [
              ["M", startPoint.x, startPoint.y],
              [
                "C",
                startPoint.x - gap,
                startPoint.y,
                startPoint.x - gap,
                endPoint.y,
                startPoint.x,
                endPoint.y,
              ],
            ],
            endArrow: true,
          },
          name: "path-shape",
        });
      }

      return shape;
    },
    afterDraw(cfg, group) {
      const labelCfg = cfg.labelCfg || {};
      const edge = group.cfg.item;
      const sourceNode = edge.getSource().getModel();
      const targetNode = edge.getTarget().getModel();
      if (sourceNode.collapsed && targetNode.collapsed) {
        return;
      }
      const path = group.find(
        (element) => element.get("name") === "path-shape"
      );

      const labelStyle = Util.getLabelPosition(path, 0.5, 0, 0, true);
      const label = group.addShape("text", {
        attrs: {
          ...labelStyle,
          text: cfg.label || '',
          fill: "#000",
          textAlign: "center",
          stroke: "#fff",
          lineWidth: 1,
        },
      });
      label.rotateAtStart(labelStyle.rotate);
    },
  }
}
