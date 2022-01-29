/**
 * @author: clay
 * @data: 2019/07/05
 * @description: register nodes
 */


import diceErBox from './dice-er-box'
const obj = {
  diceErBox
}

export default {
  obj,
  register(G6) {
    Object.values(obj).map(item => {
      G6.registerNode(item.name, item.options, item.extendName)
    })
  }
}
