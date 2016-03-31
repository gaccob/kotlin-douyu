package robott.dy.protocol

import org.apache.log4j.Logger
import robott.dy.event.SocketEventType
import robott.dy.protocol.clause.Clause

/**
 * Socket protocol for douyu
 * Created by linpeng on 2016/3/31.
 */
object DouyuSocketProtocol : Protocol {
    internal val logger = Logger.getLogger(this.javaClass)

    var clauses: Array<Clause> = emptyArray<Clause>()

    override fun addClause(clause: Clause): Protocol {
        clauses = arrayOf(clause).plus(clauses)
        logger.debug("Clause %s loaded !".format(clause))
        return this
    }

    override fun getClause(type: SocketEventType): List<Clause> {
        return clauses.filter { t -> t.clauseFor().equals(type) }
    }
}