<script>
export default {
  name: "MenuItem",
  functional: true,
  props: {
    icon: {
      type: String,
      default: ""
    },
    iconSub: {
      type: String,
      default: "el-icon-setting"
    },
    title: {
      type: String,
      default: ""
    }
  },
  render(h, context) {
    const { icon,iconSub, title } = context.props;
    const vnodes = [];

    if (icon) {
      vnodes.push(<svg-icon icon-class={icon} />);
    }

    if (title || iconSub) {
      vnodes.push(
        <span slot="title">
          <i class={iconSub} ></i> {title}
        </span>
      );
    }

    return vnodes;
  }
};
</script>
