/**
 * @author: clay
 * @data: 2021/5/18 14:21
 * @email: clay@hchyun.com
 * @description: node
 */
let vm = null;
const sendThis = (_this) => {
  vm = _this;
};

export default {
  sendThis,
  name: 'dice-er-edge',
  options: {
    getDefaultCfg() {
      return {
        multiple: true,
      };
    },
    getEvents() {
      return {
        "edge:click": "onEdgeClick",
      }
    },
    onEdgeClick(event) {
      let clickEdge = event.item;
      console.log(789)
      console.log(clickEdge);
      clickEdge.setState("selected", !clickEdge.hasState("selected"));
      console.log(clickEdge);
      let model = clickEdge.getModel();
      console.log(model);
      model.label = "4564654"
      model.stroke='#e2e2e2'
      clickEdge.update(model)

      // console.log(vm)
      // vm.currentFocus = "edge";
      this.updateVmData(event);
    },
  }
}
