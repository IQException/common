- @AutoHandleException：自动处理方法异常，可以注解类也可以注解方法。
- errorcode：区分业务异常（大于0）与系统异常（小于0），并给每个业务种类分配不同的异常码前缀，方便查询问题。
- messagesource：异常码与异常信息的转换，支持i18n，配合配置中心可以动态修改。