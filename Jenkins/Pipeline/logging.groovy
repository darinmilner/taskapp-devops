import java.util.logging.Logger


class PipelineLogger {
    Logger logger = Logger.getLogger("Task API Pipeline Logger.")

    def logInfo(message) {
        logger.info(message)
    }
}

return new PipelineLogger()