/**
 * @author: clay
 * @data: 2019/07/18
 * @description: register edges
 */


import diceErEdge from './dice-er-edge'

const obj = {
  diceErEdge
}

export default function(G6) {
  Object.values(obj).map(item => {
    G6.registerEdge(item.name, item.options, item.extendName)
  })
}
