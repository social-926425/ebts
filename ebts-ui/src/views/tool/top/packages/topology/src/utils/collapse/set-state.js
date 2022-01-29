/**
 * @author: clay
 * @data: 2021/5/11 17:28
 * @email: clay@hchyun.com
 * @description: node
 */
export default function(e){
  const {
    graph
  } = this;
  const {
    y
  } = e;
  const item = e.item;
  const shape = e.shape;
  if (!item) {
    return;
  }

  if (shape.get("name") === "collapse") {
    graph.updateItem(item, {
      collapsed: true,
      size: [300, 50],
    });
    setTimeout(() => graph.layout(), 100);
  } else if (shape.get("name") === "expand") {
    graph.updateItem(item, {
      collapsed: false,
      size: [300, 500],
    });
    setTimeout(() => graph.layout(), 100);
  }
}
