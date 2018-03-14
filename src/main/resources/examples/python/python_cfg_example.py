class GalaxyBaseRunResponse(SuccessfulRunResponse):

    def _wait_for_invocation(ctx, gi, history_id, workflow_id, invocation_id):
        final_state = wait_on(get_state, "state", timeout=60 * 60 * 24)
        if has_jobs_in_states(gi, history_id, ["new", "upload", "waiting", "queued", "running"]):
            statement()
        else:
            statement()
            return None
        gi.timeout = 60
        return cwl.run_cwltool(self._ctx, path, job_path, **self._kwds)
