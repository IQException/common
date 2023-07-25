# tuskerr

>Don't mess with Tuskerr!

业务开发中很典型的一个场景就是批处理，通常的流程是：获取任务->处理->根据处理进度取下一批任务->处理下一批任务，循环往复，直至任务处理完。

虽然看起来很简单，但是如果要做到可扩展和高性能还是需要考虑到一些问题。比如，如何将任务合理地分配给每台机器？每台机器又如何分配个每个线程？如何跟踪任务进度？如何实现每个任务至少一次、至多一次、恰好一次的处理要求？

这里我的思路是这样的：

**可扩展**：这点可以用任务调度平台提供分片信息，现在的调度平台一般都可以提供此功能。这里有个问题，如果集群中某个机器挂掉了怎么办？重新创建的节点能不能继续之前的任务。这点一般来说是没问题的，实现起来也不麻烦。

**高性能**：通过增加机器就可以获得处理能力的线性提升，每个节点也可以用多线程处理。

**跟踪任务进度**：分布式存储。

*至少一次*：完成任务再提交进度，任务提交失败报错，但继续获取新任务处理，下次提交新进度。
*至多一次*：获取任务时直接提交进度，任务提交失败报错退出。
*恰好一次*：每完成一个任务提交一次进度，任务失败报错退出。

业务开发中最常见的需求还是*至少一次*，处理逻辑自身保证幂等。
