import theme from '../../theme'




export default function(group,label,x,y,i) {
  const themeStyle = theme.defaultStyle // todo...先使用默认主题，后期可能增加其它风格的主体
  let anchorBgShape = group.addShape('marker', {
    id: label+ '_anchor_bg_lift_' + i,
    attrs: {
      name: 'anchorBg',
      x: x,
      y: y,
      ...themeStyle.anchorBgStyle.default
    },
    draggable: false,
    name: 'markerBg-shape'
  })

  let anchorShape = group.addShape('marker', {
    id: label+ '_anchor_bg_lift_' + i,
    attrs: {
      name: 'anchor',
      x: x,
      y: y,
      ...themeStyle.anchorStyle.default
    },
    draggable: false,
    name: 'marker-shape'
  })

  anchorShape.on('mouseenter', function() {
    anchorBgShape.attr({
      ...themeStyle.anchorBgStyle.active
    })
  })
  anchorShape.on('mouseleave', function() {
    anchorBgShape.attr({
      ...themeStyle.anchorBgStyle.inactive
    })
  })


}
