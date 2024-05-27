import AuthService from "@/api/auth.service";
import { Button } from "@/components/ui/button";
import {
  Form,
  FormField,
  FormItem,
  FormLabel,
  FormControl,
  FormMessage,
} from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { zodResolver } from "@hookform/resolvers/zod";
import { LoaderCircle } from "lucide-react";
import { SubmitHandler, useForm } from "react-hook-form";
import { Link, useNavigate } from "react-router-dom";
import { z } from "zod";

const registerSchema = z
  .object({
    firstName: z.string().min(1, { message: "First name is required" }),
    lastName: z.string().min(1, { message: "Last name is required" }),
    email: z.string().email("The email address is invalid"),
    username: z
      .string()
      .min(3, "Username must be between 3 and 20 characters")
      .max(20, "Username must be between 3 and 20 characters")
      .regex(/^[a-zA-Z0-9_-]*$/, "Use letters, numbers, '-' or '_'."),
    password: z
      .string()
      .regex(
        /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,30}$/,
        "Length must be between 8 and 30 characters, use at least 1 uppercase letter, 1 lowercase letter, 1 number.",
      ),
    passwordRepeat: z.string().min(1, { message: "Password confirmation is required" }),
  })
  .refine((data) => data.password === data.passwordRepeat, {
    message: "Passwords must match",
    path: ["passwordRepeat"],
  });

type RegisterData = z.infer<typeof registerSchema>;

const RegisterPage = () => {
  const navigate = useNavigate();
  const form = useForm<RegisterData>({
    mode: "onTouched",
    defaultValues: {
      firstName: "",
      lastName: "",
      email: "",
      username: "",
      password: "",
      passwordRepeat: "",
    },
    criteriaMode: "all",
    resolver: zodResolver(registerSchema),
  });
  const { mutateAsync: register } = AuthService.useRegister();

  const onSubmit: SubmitHandler<RegisterData> = async (values) => {
    form.clearErrors("root.serverError");
    try {
      await register(
        {
          ...values,
          email: values.email.trim(),
          username: values.username.trim(),
        },
        {
          onSuccess: () => navigate("/login"),
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
    <div className="container flex justify-center py-4">
      <div className="bg-card shadow p-8 w-full md:w-[550px] border border-border rounded-md">
        <Form {...form}>
          <h3 className="mb-4">Register</h3>
          <form
            onSubmit={form.handleSubmit(onSubmit)}
            className="grid-cols-1 grid sm:grid-cols-2 gap-4"
            autoComplete="off"
          >
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
              name="email"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Email</FormLabel>
                  <FormControl>
                    <Input {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="firstName"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>First name:</FormLabel>
                  <FormControl>
                    <Input {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="lastName"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Last name</FormLabel>
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

            <FormField
              control={form.control}
              name="passwordRepeat"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Confirm Password</FormLabel>
                  <FormControl>
                    <Input type="password" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            {form.formState.errors.root?.serverError.message && (
              <span className="text-destructive text-sm">
                {form.formState.errors.root?.serverError.message}
              </span>
            )}
            <Button
              className="w-full sm:col-span-2"
              type="submit"
              disabled={form.formState.isSubmitting}
            >
              {form.formState.isSubmitting ? <LoaderCircle className="animate-spin" /> : "Register"}
            </Button>
          </form>
          <p className="text-center text-sm">
            Already have an account?{" "}
            <Link to="/login" className="text-primary font-semibold underline">
              Login here!
            </Link>
          </p>
        </Form>
      </div>
    </div>
  );
};

export default RegisterPage;
