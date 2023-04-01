package trading.trace

import trading.shared.*

import natchez.Kernel

type CmdKernel = CmdKernel.Type
object CmdKernel extends Newt[Kernel]

type EvtKernel = EvtKernel.Type
object EvtKernel extends Newt[Kernel]
