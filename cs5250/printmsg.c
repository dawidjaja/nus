#include <linux/kernel.h> /* for printk */
#include <linux/syscalls.h> /* for SYSCALL_DEFINE1 macro */
SYSCALL_DEFINE1(printmsg, int, i)
{
    printk(KERN_DEBUG "Hello! This is A0184588J from %d", i);
    return 1;
}
