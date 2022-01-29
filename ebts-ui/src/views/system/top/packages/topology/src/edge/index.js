/**
 * @author: clay
 * @data: 2019/07/18
 * @description: register edges
 */

import Line from './top-line'
import Brokenline from './top-brokenline'
import Polyline from './top-polyline'
import Cubic from './top-cubic'

import diceErEdge from './dice-er-edge'

const obj = {
  // Line,
  // Brokenline,
  // Polyline,
  // Cubic
  diceErEdge
}

export default function(G6) {
  Object.values(obj).map(item => {
    G6.registerEdge(item.name, item.options, item.extendName)
  })
}
