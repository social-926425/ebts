/**
 * @author: clay
 * @data: 2021/5/13 23:14
 * @email: clay@hchyun.com
 * @description: node
 */


export default {
  name: "wheel-node",
  options: {
    getEvents() {
      return {
        wheel: "scorll"
      };
    },
    scorll(e) {
      console.log(456)
      e.preventDefault();
      const {
        graph
      } = this;
      const nodes = graph.getNodes().filter((n) => {
        const bbox = n.getBBox();

        return isInBBox(graph.getPointByClient(e.clientX, e.clientY), bbox);
      });
      if (nodes) {
        nodes.forEach((node) => {
          const model = node.getModel();
          if (model.attrs.length < 9) {
            return;
          }
          const idx = model.startIndex || 0;
          let startX = model.startX || 0.5;
          let startIndex = idx + e.deltaY * 0.02;
          startX -= e.deltaX;
          if (startIndex < 0) {
            startIndex = 0;
          }
          if (startX > 0) {
            startX = 0;
          }
          if (startIndex > model.attrs.length - 1) {
            startIndex = model.attrs.length - 1;
          }
          graph.update(node, {
            startIndex,
            startX,
          });
        });
      }


    },
  }
};

