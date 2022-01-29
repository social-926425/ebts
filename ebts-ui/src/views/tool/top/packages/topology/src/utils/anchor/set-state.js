/**
 * @author: clay
 * @data: 2019/08/15
 * @description: set anchor state
 */
import theme from '../../theme'

export default function(graph,name, value, item) {

  const themeStyle = theme.defaultStyle // todo...先使用默认主题，后期可能增加其它风格的主体
  if (name === 'hover') {
    let group = item.getContainer()
    let children = group.get('children')
    for (let i = 0, len = children.length; i < len; i++) {
      let child = children[i]
      // 处理锚点状态
      if (child.attrs.name === 'anchorg') {
        if (value) {
          console.log(graph)
          child.attr(themeStyle.anchorStyle.hover)
        } else {
          child.attr(themeStyle.anchorStyle.unhover)
        }
      }
    }
  }
}
