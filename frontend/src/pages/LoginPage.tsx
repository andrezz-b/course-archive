import { zodResolver } from "@hookform/resolvers/zod";
import { SubmitHandler, useForm } from "react-hook-form";
import * as z from "zod";
import { Button } from "@/components/ui/button";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { Link, useLocation, useNavigate } from "react-router-dom";
import AuthService from "@/api/auth.service";
import { LoaderCircle } from "lucide-react";

const formSchema = z.object({
  username: z.string().min(1, {
    message: "Username is required",
  }),
  password: z.string().min(1, {
    message: "Password is required",
  }),
});

type LoginData = z.infer<typeof formSchema>;

const LoginPage = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const previousLocation = location?.state?.from?.pathname ?? "/";
  const form = useForm<LoginData>({
    mode: "onBlur",
    defaultValues: {
      username: "",
      password: "",
    },
    criteriaMode: "all",
    resolver: zodResolver(formSchema),
  });
  const { mutateAsync: login } = AuthService.useLogin();
  const onSubmit: SubmitHandler<LoginData> = async (values) => {
    form.clearErrors("root.serverError");
    try {
      await login(
        {
          username: values.username.trim(),
          password: values.password,
        },
        {
          onSuccess: () => navigate(previousLocation),
          onError: (error) =>
            form.setError("root.serverError", {
              type: error.getStatusCode().toString(),
              message: error.getErrorMessage(),
            }),
        },
      );
    } catch (error) {
      // Handled by onError
    }
  };

  return (
    <div className="flex flex-col flex-grow justify-center items-center max-h-[35rem] px-4">
      <div className="bg-card shadow p-8 w-full md:w-[400px] border border-border rounded-md">
        <Form {...form}>
          <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4" autoComplete="off">
            <h3>Login</h3>
            <FormField
              control={form.control}
              name="username"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Username</FormLabel>
                  <FormControl>
                    <Input {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="password"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Password</FormLabel>
                  <FormControl>
                    <Input type="password" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            {form.formState.errors.root?.serverError.message && (
              <p className="text-destructive text-sm">
                {form.formState.errors.root?.serverError.message}
              </p>
            )}
            <Button className="w-full" type="submit">
              {form.formState.isSubmitting ? <LoaderCircle className="animate-spin" /> : "Login"}
            </Button>
            <p className="text-center">
              Don't have an account?{" "}
              <Link to="/register" className="text-primary font-semibold">
                Register here!
              </Link>
            </p>
          </form>
        </Form>
      </div>
    </div>
  );
};

export default LoginPage;
