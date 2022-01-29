/**
 * @author: clay
 * @data: 2019/07/16
 * @description: register behaviors
 */

import dragAddEdge from './drag-add-edge'
import hoverEventEdit from './hover-event-edit'
import clickEventEdit from './click-event-edit'
import dragEventEdit from './drag-event-edit'
import keyupEventEdit from './keyup-event-edit'
import diceErNode from './dice-er-node'
import diceErEdge from './dice-er-edge'

const obj = {
  // dragAddEdge,
  hoverEventEdit,
  // clickEventEdit,
  dragEventEdit,
  keyupEventEdit,
  diceErNode,
  diceErEdge
}

export default {
  obj,
  register(G6) {
    Object.values(obj).map(item => {
      G6.registerBehavior(item.name, item.options)
    })
  }
}
