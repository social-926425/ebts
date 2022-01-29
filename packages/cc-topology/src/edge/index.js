/**
 * @author: winyuan
 * @data: 2019/07/18
 * @repository: https://github.com/winyuan
 * @description: register edges
 */

import ccCubic from './cc-cubic'
import dottedline from './dottedline'
import solidline from './solidline'
import crudedottedline from './crudedottedline'

const obj = {
  ccCubic,
  dottedline,
  solidline,
  crudedottedline
}

export default function(G6) {
  Object.values(obj).map(item => {
    G6.registerEdge(item.name, item.options, item.extendName)
  })
}
