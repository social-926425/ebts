/**
 * @author: clay
 * @data: 2019/07/16
 * @description: edit mode: 悬浮交互
 */
// 用来获取调用此js的vue组件实例（this）
let vm = null;
const sendThis = (_this) => {
  vm = _this;
};
export default {
  sendThis, // 暴露函数
  name: "hover-event-edit",
  options: {
    getEvents() {
      return {
        "node:mouseover": "onNodeHover",
        "node:mouseout": "onNodeOut",
      };
    },
    onNodeHover(event) {
      let graph = vm.graph;
      let hoverNode = event.item;

      const name = event.shape.get("name");
      const item = event.item;

      if (name && name.startsWith("item")) {
        graph.updateItem(item, {
          selectedIndex: Number(name.split("-")[1])
        });
      } else {
        graph.updateItem(item, {
          selectedIndex: NaN
        });
      }
      // console.log(item);
      hoverNode.setState("hover", true);
    },
    onNodeOut(event) {
      let hoverNode = event.item;
      hoverNode.setState("hover", false);
    }
  }
};
